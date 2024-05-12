<template>
  <div>
    <!-- 表头分页查询条件， shadow="never" 指定 card 卡片组件没有阴影 -->
    <el-card shadow="true" class="mb-5">
      <!-- flex 布局，内容垂直居中 -->
      <div class="flex items-center">
        <el-text>用户名称</el-text>
        <div class="ml-3 w-52 mr-5">
          <el-input v-model="searchCategoryName" placeholder="请输入（模糊查询）"/>
        </div>

        <el-text>创建日期</el-text>
        <div class="ml-3 w-30 mr-5">
          <!-- 日期选择组件（区间选择） -->
          <el-date-picker v-model="pickDate" type="daterange" range-separator="至" start-placeholder="开始时间"
                          end-placeholder="结束时间" size="default" :shortcuts="shortcuts" @change="datepickerChange"/>
        </div>

        <el-button type="primary" class="ml-3" :icon="Search" @click="getTableData">查询</el-button>
        <el-button class="ml-3" :icon="RefreshRight" @click="reset">重置</el-button>
      </div>
    </el-card>


    <el-card shadow="never">
      <div class="mb-5">
        <el-button type="primary" @click="addUserClick">
          <el-icon class="mr-1">
            <Plus/>
          </el-icon>
          新增
        </el-button>
      </div>

      <!-- 菜单列表 -->
      <el-table :data="tableData" border stripe style="width: 100%" v-loading="tableLoading">
        <el-table-column prop="username" label="用户名称" width="115">
          <template #default="scope">
            <el-tag class="ml-2" type="success">{{ scope.row.username }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="role" label="用户角色" width="180">
          <template #default="scope">
            <el-tag class="ml-2" type="primary">{{ scope.row.role }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="scope">
            <el-tag class="ml-2" type="info">{{ scope.row.createTime }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="updateTime" label="更新时间" width="180">
          <template #default="scope">
            <el-tag class="ml-2" type="info">{{ scope.row.updateTime }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="primary" size="small" @click="updateUserInfo(scope.row.username,scope.row.role)">
              <el-icon class="mr-1">
                <Edit/>
              </el-icon>
              修改
            </el-button>
          </template>
        </el-table-column>
      </el-table>


      <!-- 添加用户 -->
      <FormDialog ref="formDialogRef" title="添加用户" destroyOnClose @submit="onSubmit">
        <el-form ref="formRef" :rules="rules" :model="form">
          <el-form-item label="用户名称" prop="username" label-width="80px" size="large">
            <el-input v-model="form.username" placeholder="请输入用户名称" maxlength="20" show-word-limit clearable/>
          </el-form-item>
          <el-form-item label="用户密码" prop="password" label-width="80px" size="large">
            <el-input v-model="form.password" placeholder="请输入用户密码" maxlength="20" show-word-limit clearable/>
          </el-form-item>
          <el-form-item label="用户角色" prop="role">
            <el-select v-model="form.role" clearable placeholder="---请选择---" size="large">
              <el-option label="超级管理员" value="ROLE_ROOT"/>
              <el-option label="管理员" value="ROLE_ADMIN"/>
              <el-option label="游客" value="ROLE_GUEST"/>
            </el-select>
          </el-form-item>
        </el-form>
      </FormDialog>


      <!-- 修改用户 -->
      <FormDialog ref="formDialogRefUpdate" title="修改用户" destroyOnClose @submit="onUpdateUserInfo">
        <el-form ref="updateFormRef" :rules="rules" :model="newForm">
          <el-form-item label="用户名称" prop="username" label-width="80px" size="large">
            <el-input v-model="newForm.username" placeholder="请输入用户名称" maxlength="20" show-word-limit clearable disabled/>
          </el-form-item>
          <el-form-item label="用户密码" prop="password" label-width="80px" size="large">
            <el-input v-model="newForm.password" placeholder="请输入用户密码" maxlength="20" show-word-limit clearable/>
          </el-form-item>
          <el-form-item label="用户角色" prop="role">
            <el-select v-model="newForm.role" clearable placeholder="---请选择---" size="large">
              <el-option label="超级管理员" value="ROLE_ROOT"/>
              <el-option label="管理员" value="ROLE_ADMIN"/>
              <el-option label="游客" value="ROLE_GUEST"/>
            </el-select>
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
import {Search, RefreshRight} from '@element-plus/icons-vue'
import {ref, reactive} from 'vue'
import FormDialog from '@/components/FormDialog'
import {showMessage} from "@/composables/util"
import {addUser, getPageUserList,updateUser} from "@/api/admin/user"
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

const formRef = ref(null)
const updateFormRef = ref(null)

const form = reactive({
  username: '',
  password: '',
  role: ''
})

const newForm = reactive({
  username: '',
  password: '',
  role: ''
})

// 表格加载 Loading
const tableLoading = ref(false)
// 对话框是否显示
const formDialogRef = ref(null)

const formDialogRefUpdate = ref(null)

// 规则校验
const rules = {
  username: [
    {
      required: true,
      message: '用户名称不能为空',
      trigger: 'blur',
    },
    {min: 1, max: 20, message: '用户字数要求大于 1 个字符，小于 20 个字符', trigger: 'blur'},
  ],
  password: [
    {
      required: true,
      message: '用户密码不能为空',
      trigger: 'blur',
    },
    {min: 1, max: 20, message: '密码字数要求大于 1 个字符，小于 20 个字符', trigger: 'blur'}
  ],
  role: [
    {
      required: true,
      message: '请选择用户角色',
      trigger: 'blur',
    }
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

// 新增用户按钮点击事件
const addUserClick = () => {
  formDialogRef.value.open()
}

const updateUserInfo = (username,role) => {
  formDialogRefUpdate.value.open()
  newForm.username = username
  newForm.role = 'ROLE_' + role

}

const onSubmit = () => {
  formRef.value.validate((valid) => {
    if (!valid) {
      showMessage('表单验证不通过', 'error')
      return false
    }

    formDialogRef.value.showBtnLoading()

    addUser(form).then((res) => {
      if (res.success === true) {
        showMessage('添加成功')
        // 将表单中分类名称置空
        form.username = ''
        form.password = ''
        form.role = ''
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

const onUpdateUserInfo = () => {
  updateFormRef.value.validate((valid) => {
    if (!valid) {
      showMessage('表单验证不通过', 'error')
      return false
    }

    formDialogRef.value.showBtnLoading()

    updateUser(newForm).then((res) => {
      if (res.success === true) {
        showMessage('用户信息修改成功')
        // 将表单中分类名称置空
        newForm.username = ''
        newForm.password = ''
        newForm.role = ''
        // 隐藏对话框
        formDialogRef.value.close()

        // 退出登录
        userStore.logout()

        // 跳转登录页
        router.replace('/login')
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
  getPageUserList({
    currentPage: current.value,
    size: size.value,
    startDate: startDate.value,
    endDate: endDate.value,
    name: searchCategoryName.value
  }).then((res) => {
    if (res.success === true) {
      tableData.value = res.data
      current.value = res.currentPage
      size.value = res.size
      total.value = res.total
    }
  }).catch(error => {})
      .finally(() => {
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