<template>
  <div>
    <!-- 表头分页查询条件， shadow="never" 指定 card 卡片组件没有阴影 -->
    <el-card shadow="true" class="mb-5">
      <!-- flex 布局，内容垂直居中 -->
      <div class="flex items-center">
        <el-text>菜单名称</el-text>
        <div class="ml-3 w-52 mr-5"><el-input v-model="searchCategoryName" placeholder="请输入（模糊查询）" /></div>

        <el-text>创建日期</el-text>
        <div class="ml-3 w-30 mr-5">
          <!-- 日期选择组件（区间选择） -->
          <el-date-picker v-model="pickDate" type="daterange" range-separator="至" start-placeholder="开始时间"
                          end-placeholder="结束时间" size="default" :shortcuts="shortcuts" @change="datepickerChange" />
        </div>

        <el-button type="primary" class="ml-3" :icon="Search" @click="getTableData">查询</el-button>
        <el-button class="ml-3" :icon="RefreshRight" @click="reset">重置</el-button>
      </div>
    </el-card>


    <el-card shadow="never">
      <!-- 新增按钮 -->
      <div class="mb-5">
        <el-button type="primary" @click="addMenuClick">
          <el-icon class="mr-1">
            <Plus />
          </el-icon>
          新增</el-button>
      </div>

      <!-- 菜单列表 -->
      <el-table :data="tableData" border stripe style="width: 100%" v-loading="tableLoading">
        <el-table-column prop="index" label="序号" width="60">
          <template #default="scope">
            <el-tag class="ml-2" type="danger">{{ scope.row.id }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="name" label="菜单名称" width="115">
          <template #default="scope">
            <el-tag class="ml-2" type="success">{{ scope.row.name }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="path" label="路由Path" width="180" >
            <template #default="scope">
              <el-tag class="ml-2" type="primary">{{ scope.row.path }}</el-tag>
            </template>
        </el-table-column>

        <el-table-column prop="icon" label="图标类型" width="150" >
          <template #default="scope">
            <el-tag class="ml-2" type="info">{{ scope.row.icon }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createTime" label="创建时间" width="180" />
<!--        <el-table-column label="操作">-->
<!--          <template #default="scope">-->
<!--            <el-button type="primary" size="small" >-->
<!--              <el-icon class="mr-1">-->
<!--                <Edit />-->
<!--              </el-icon>-->
<!--              修改-->
<!--            </el-button>-->
<!--            <el-button type="danger" size="small">-->
<!--              <el-icon class="mr-1">-->
<!--                <Delete />-->
<!--              </el-icon>-->
<!--              删除-->
<!--            </el-button>-->
<!--          </template>-->
<!--        </el-table-column>-->
      </el-table>


      <!-- 添加菜单 -->
      <FormDialog ref="formDialogRef" title="添加菜单" destroyOnClose @submit="onSubmit">
        <el-form ref="formRef" :rules="rules" :model="form">
          <el-form-item label="菜单名称" prop="name" label-width="80px" size="large">
            <el-input v-model="form.name" placeholder="请输入菜单名称" maxlength="6" show-word-limit clearable/>
          </el-form-item>
          <el-form-item label="路由路径" prop="path" label-width="80px" size="large">
            <el-input v-model="form.path" placeholder="请输入路由Path" maxlength="15" show-word-limit clearable/>
          </el-form-item>
          <el-form-item label="图标类型" prop="icon" label-width="80px" size="large">
            <el-input v-model="form.icon" placeholder="请输入菜单图标类型" maxlength="8" show-word-limit clearable/>
          </el-form-item>
        </el-form>
      </FormDialog>


      <!-- 分页 -->
      <div class="mt-10 flex justify-center">
        <el-pagination v-model:current-page="current" v-model:page-size="size" :page-sizes="[10, 20, 50]"
                       :small="false" :background="true" layout="total, sizes, prev, pager, next, jumper"
                       :total="total" @size-change="handleSizeChange" @current-change="getTableData"/>
      </div>

    </el-card>

  </div>
</template>

<script setup>
import { Search, RefreshRight } from '@element-plus/icons-vue'
import { ref,reactive } from 'vue'
import {addMenu, getAllMenu, getPageMenuList} from '@/api/admin/menu'
import FormDialog from '@/components/FormDialog'
import {showMessage} from "@/composables/util";


const formRef = ref(null)

const form = reactive({
  // index: '',
  name: '',
  path: '',
  icon: ''
  // createTime: ''
})
// 表格加载 Loading
const tableLoading = ref(false)
// 对话框是否显示
const formDialogRef = ref(null)

// 规则校验
const rules = {
  name: [
    {
      required: true,
      message: '分类名称不能为空',
      trigger: 'blur',
    },
    { min: 1, max: 6, message: '菜单名称字数要求大于 1 个字符，小于 6 个字符', trigger: 'blur' },
  ],
  path: [
    {
      required: true,
      message: '路由Path不能为空',
      trigger: 'blur',
    },
    {min: 1, max: 15, message: '路由Path字数要求大于 1 个字符，小于 15 个字符', trigger: 'blur'}
  ],
  icon: [
    {
      required: true,
      message: '菜单图标类型不能为空',
      trigger: 'blur',
    },
    {min: 1, max: 8, message: '菜单图标类型字数要求大于 1 个字符，小于 8 个字符', trigger: 'blur'}
  ]
}

// 分页查询的分类名称
const searchCategoryName = ref('')
// 日期
const pickDate = ref('')

// 表格数据
const tableData = ref([])

// 当前页码，给了一个默认值 1
const current = ref(1)
// 总数据量，给了个默认值 0
const total = ref(0)
// 每页显示的数据量，给了个默认值 10
const size = ref(10)

// 查询条件：开始结束时间
const startDate = reactive({})
const endDate = reactive({})


// 重置查询条件
const reset = () => {
  searchCategoryName.value = ''
  pickDate.value = ''
  startDate.value = null
  endDate.value = null
}


// 新增菜单按钮点击事件
const addMenuClick = () => {
  formDialogRef.value.open()
}


const onSubmit = () => {
  formRef.value.validate((valid) => {
    if (!valid) {
      showMessage('表单验证不通过','error')
      return false
    }

    formDialogRef.value.showBtnLoading()

    addMenu(form).then((res) => {
      if (res.success === true) {
        showMessage('添加成功')
        // 将表单中分类名称置空
        form.name = ''
        form.path = ''
        form.icon = ''
        // 隐藏对话框
        // dialogVisible.value = false
        formDialogRef.value.close()
        // 重新请求分页接口，渲染数据
        getTableData()
      }
    }).catch(error => {
      // showMessage('添加失败','error')
    }).finally(() => {
      formDialogRef.value.closeBtnLoading()
    })
  })
}

// 获取分页数据
function getTableData() {
  tableLoading.value = true
  // 调用后台分页接口，并传入所需参数
  getPageMenuList({currentPage: current.value, size: size.value, startDate: startDate.value, endDate: endDate.value, name: searchCategoryName.value}).then((res) => {
        if (res.success === true) {
          tableData.value = res.data
          current.value = res.currentPage
          size.value = res.size
          total.value = res.total
        }
      }).catch(error => {}
  ).finally(() => {
    tableLoading.value = false
  })
}
getTableData()

const shortcuts = [
  {
    text: '最近一周',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    },
  },
  {
    text: '最近一个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    },
  },
  {
    text: '最近三个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      return [start, end]
    },
  },
]
</script>

<style scoped>

</style>