<style lang="less">
@import "../../../styles/tree-common.less";
</style>
<template>
<div class="search">
    <Card>
        <Row class="operation">
            <Button @click="add" type="primary" icon="md-add" ghost shape="circle">添加子部门</Button>
            <Button @click="addRoot" icon="md-add" type="primary" ghost shape="circle">添加一级部门</Button>
            <Button @click="delAll" icon="md-trash" type="error" ghost shape="circle">批量删除</Button>
            <Button @click="getParentList" icon="md-refresh" type="info" ghost shape="circle">刷新</Button>
            <Button @click="excelData" type="success" icon="md-paper-plane" ghost shape="circle">导出部门用户</Button>
            <i-switch v-model="strict" size="large" style="margin-left:5px">
                <span slot="open">级联</span>
                <span slot="close">单选</span>
            </i-switch>
        </Row>
        <Row type="flex" justify="start">
            <Col :md="8" :lg="8" :xl="6">
            <Alert show-icon>
                当前选择编辑：
                <span class="select-title">{{editTitle}}</span>
                <a class="select-clear" v-if="form.id" @click="cancelEdit">取消选择</a>
            </Alert>
            <Input v-model="searchKey" suffix="ios-search" @on-change="search" placeholder="输入部门名搜索" clearable />
            <div class="tree-bar" :style="{maxHeight: maxHeight}">
                <Tree ref="tree" :data="data" :load-data="loadData" show-checkbox @on-check-change="changeSelect" @on-select-change="selectTree" :check-strictly="!strict"></Tree>
                <Spin size="large" fix v-if="loading"></Spin>
            </div>
            </Col>
            <Col :md="15" :lg="13" :xl="9" style="margin-left:10px;">
            <Form ref="form" :model="form" :label-width="100" :rules="formValidate">
                <FormItem label="上级部门" prop="parentTitle">
                    <div style="display:flex;">
                        <Input v-model="form.parentTitle" readonly style="margin-right:10px;" />
                        <Poptip transfer trigger="click" placement="right-start" title="选择上级部门" width="250">
                            <Button icon="md-list">选择部门</Button>
                            <div slot="content" style="position:relative;min-height:5vh">
                                <Tree :data="dataEdit" :load-data="loadData" @on-select-change="selectTreeEdit"></Tree>
                                <Spin size="large" fix v-if="loadingEdit"></Spin>
                            </div>
                        </Poptip>
                    </div>
                </FormItem>
                <FormItem label="部门名称" prop="title">
                    <Input v-model="form.title" />
                </FormItem>
                <FormItem label="部门负责人" prop="mainHeader">
                    <Select :loading="userLoading" not-found-text="该部门暂无用户数据" v-model="form.mainHeader" multiple filterable placeholder="请选择或输入搜索用户">
                        <Option v-for="item in users" :value="item.id" :key="item.id" :label="item.nickname">
                            <span style="margin-right:10px;">{{ item.nickname }}</span>
                            <span style="color:#ccc;">{{ item.username }}</span>
                        </Option>
                    </Select>
                </FormItem>
                <FormItem label="副负责人" prop="viceHeader">
                    <Select :loading="userLoading" not-found-text="该部门暂无用户数据" v-model="form.viceHeader" multiple filterable placeholder="请选择或输入搜索用户">
                        <Option v-for="item in users" :value="item.id" :key="item.id" :label="item.nickname">
                            <span style="margin-right:10px;">{{ item.nickname }}</span>
                            <span style="color:#ccc;">{{ item.username }}</span>
                        </Option>
                    </Select>
                </FormItem>
                <FormItem label="排序值" prop="sortOrder">
                    <Tooltip trigger="hover" placement="right" content="值越小越靠前，支持小数">
                        <InputNumber :max="1000" :min="0" v-model="form.sortOrder"></InputNumber>
                    </Tooltip>
                </FormItem>
                <FormItem label="是否启用" prop="status">
                    <i-switch size="large" v-model="form.status" :true-value="0" :false-value="-1">
                        <span slot="open">启用</span>
                        <span slot="close">禁用</span>
                    </i-switch>
                </FormItem>
                <Form-item class="br">
                    <Button @click="submitEdit" :loading="submitLoading" type="primary" ghost shape="circle" icon="ios-create-outline">修改并保存</Button>
                    <Button @click="handleReset" type="info" ghost shape="circle">重置</Button>
                </Form-item>
            </Form>
            </Col>
        </Row>
    </Card>
    <Divider dashed />
    <Card>
        <Row>
            <Table :loading="userLoading" border :columns="userColumns" :data="userData" ref="table" sortable="custom" @on-sort-change="changeSort" @on-selection-change="changeSelect" @on-row-click="rowClick" :row-class-name="rowClassNmae"></Table>
        </Row>
        <Row type="flex" justify="end" class="page">
            <Page :current="searchForm.pageNumber" :total="userTotal" :page-size="searchForm.pageSize" @on-change="changePage" @on-page-size-change="changePageSize" :page-size-opts="[10,20,50]" size="small" show-total show-elevator show-sizer></Page>
        </Row>
    </Card>

    <Modal :title="modalTitle" v-model="modalVisible" :mask-closable="false" :width="500">
        <Form ref="formAdd" :model="formAdd" :label-width="85" :rules="formValidate">
            <div v-if="showParent">
                <FormItem label="上级部门：">{{form.title}}</FormItem>
            </div>
            <FormItem label="部门名称" prop="title">
                <Input v-model="formAdd.title" />
            </FormItem>
            <FormItem label="排序值" prop="sortOrder">
                <Tooltip trigger="hover" placement="right" content="值越小越靠前，支持小数">
                    <InputNumber :max="1000" :min="0" v-model="formAdd.sortOrder"></InputNumber>
                </Tooltip>
            </FormItem>
            <FormItem label="是否启用" prop="status">
                <i-switch size="large" v-model="formAdd.status" :true-value="0" :false-value="-1">
                    <span slot="open">启用</span>
                    <span slot="close">禁用</span>
                </i-switch>
            </FormItem>
        </Form>
        <div slot="footer">
            <Button type="text" @click="cancelAdd">取消</Button>
            <Button type="primary" :loading="submitLoading" @click="submitAdd">提交</Button>
        </div>
    </Modal>
</div>
</template>

<script>
import {
    initDepartment,
    loadDepartment,
    addDepartment,
    editDepartment,
    deleteDepartment,
    searchDepartment,
    getUserByDepartmentId,
    getMyUserListData
} from "@/api/index";
export default {
    name: "department-manage",
    data() {
        return {
            userLoading: true, // 表单加载状态
            searchForm: { // 搜索框初始化对象
                pageNumber: 1, // 当前页数
                pageSize: 10, // 页面大小
                sort: "createTime", // 默认排序字段
                order: "desc", // 默认排序方式
                departmentId: ""
            },
            userData: [],
            userTotal: 0,
            selectList: [], // 多选数据
            selectCount: 0, // 多选计数
            selectRow: 0,
            loading: true,
            maxHeight: "500px",
            strict: true,
            userLoading: false,
            loadingEdit: true,
            modalVisible: false,
            selectList: [],
            selectCount: 0,
            showParent: false,
            modalTitle: "",
            editTitle: "",
            searchKey: "",
            form: {
                id: "",
                title: "",
                parentId: "",
                parentTitle: "",
                sortOrder: 0,
                status: 0
            },
            formAdd: {},
            formValidate: {
                title: [{
                    required: true,
                    message: "名称不能为空",
                    trigger: "blur"
                }],
                sortOrder: [{
                    required: true,
                    type: "number",
                    message: "排序值不能为空",
                    trigger: "blur"
                }]
            },
            submitLoading: false,
            data: [],
            dataEdit: [],
            users: [],
            userColumns: [{
                    type: "selection",
                    width: 60,
                    align: "center",
                    fixed: "left"
                },
                {
                    type: "index",
                    width: 60,
                    align: "center",
                    fixed: "left"
                },
                {
                    title: "登录账号",
                    key: "username",
                    minWidth: 125,
                    sortable: true,
                    fixed: "left"
                },
                {
                    title: "用户名",
                    key: "nickname",
                    minWidth: 125,
                    sortable: true,
                    fixed: "left",
                    render: (h, params) => {
                        return h(
                            "a", {
                                on: {
                                    click: () => {
                                        console.log(params.row);
                                    }
                                }
                            },
                            params.row.nickname
                        );
                    }
                },
                {
                    title: "头像",
                    key: "avatar",
                    width: 80,
                    align: "center",
                    render: (h, params) => {
                        return h("Avatar", {
                            props: {
                                src: params.row.avatar
                            }
                        });
                    }
                },
                {
                    title: "所属部门",
                    key: "departmentTitle",
                    minWidth: 120
                },
                {
                    title: "手机",
                    key: "mobile",
                    minWidth: 125,
                    sortable: true
                },
                {
                    title: "邮箱",
                    key: "email",
                    minWidth: 180,
                    sortable: true
                },
                {
                    title: "性别",
                    key: "sex",
                    width: 70,
                    align: "center"
                },
                {
                    title: "类型",
                    key: "type",
                    align: "center",
                    width: 100,
                    render: (h, params) => {
                        let re = "";
                        if (params.row.type == 1) {
                            re = "管理员";
                        } else if (params.row.type == 0) {
                            re = "普通用户";
                        }
                        return h("div", re);
                    },
                    filters: [{
                            label: "普通用户",
                            value: 0
                        },
                        {
                            label: "管理员",
                            value: 1
                        }
                    ],
                    filterMultiple: false,
                    filterRemote: e => {
                        let v = "";
                        if (e.length > 0) {
                            v = e[0];
                        }
                        this.searchForm.type = v;
                        this.searchForm.pageNumber = 1;
                        this.getUserList();
                    }
                },
                {
                    title: "状态",
                    key: "status",
                    align: "center",
                    width: 110,
                    render: (h, params) => {
                        if (params.row.status == 0) {
                            return h("div", [
                                h("Badge", {
                                    props: {
                                        status: "success",
                                        text: "正常启用"
                                    }
                                })
                            ]);
                        } else if (params.row.status == -1) {
                            return h("div", [
                                h("Badge", {
                                    props: {
                                        status: "error",
                                        text: "禁用"
                                    }
                                })
                            ]);
                        }
                    },
                    filters: [{
                            label: "正常启用",
                            value: 0
                        },
                        {
                            label: "禁用",
                            value: -1
                        }
                    ],
                    filterMultiple: false,
                    filterRemote: e => {
                        let v = "";
                        if (e.length > 0) {
                            v = e[0];
                        }
                        this.searchForm.status = v;
                        this.searchForm.pageNumber = 1;
                        this.getUserList();
                    }
                },
                {
                    title: "创建时间",
                    key: "createTime",
                    sortable: true,
                    sortType: "desc",
                    width: 180
                }
            ],
            columns: [{
                    type: "selection",
                    width: 60,
                    align: "center"
                },
                {
                    type: "index",
                    width: 60,
                    align: "center"
                },
                {
                    title: "部门名称",
                    key: "title",
                    minWidth: 120,
                    sortable: true,
                    tree: true
                },
                {
                    title: "排序",
                    key: "sortOrder",
                    width: 150,
                    sortable: true,
                    align: "center",
                    sortType: "asc"
                },
                {
                    title: "创建时间",
                    key: "createTime",
                    sortable: true,
                    width: 200
                },
                {
                    title: "操作",
                    key: "action",
                    width: 300,
                    align: "center",
                    render: (h, params) => {
                        return h("div", [
                            h(
                                "Button", {
                                    props: {
                                        type: "primary",
                                        size: "small",
                                        icon: "md-add"
                                    },
                                    style: {
                                        marginRight: "5px"
                                    },
                                    on: {
                                        click: () => {
                                            this.tableAdd(params.row);
                                        }
                                    }
                                },
                                " 添加子部门"
                            ),
                            h(
                                "Button", {
                                    props: {
                                        type: "error",
                                        size: "small"
                                    },
                                    on: {
                                        click: () => {
                                            this.remove(params.row);
                                        }
                                    }
                                },
                                "删除"
                            )
                        ]);
                    }
                }
            ]
        };
    },
    methods: {
        init() {
            this.getParentList();
            this.getParentListEdit();
            this.getUserDataList();
        },
        getUserDataList() {
            var that = this;
            that.userLoading = true;
            getMyUserListData(that.searchForm).then(res => {
                that.userLoading = false;
                if (res.success) {
                    that.userData = res.result.records;
                    that.userTotal = res.result.total;
                }
            });
        },
        getParentList() {
            this.loading = true;
            initDepartment().then(res => {
                this.loading = false;
                if (res.success) {
                    res.result.forEach(function (e) {
                        if (e.isParent) {
                            e.loading = false;
                            e.children = [];
                            e._loading = false;
                        }
                    });
                    this.data = res.result;
                }
            });
        },
        getParentListEdit() {
            this.loadingEdit = true;
            initDepartment().then(res => {
                this.loadingEdit = false;
                if (res.success) {
                    res.result.forEach(function (e) {
                        if (e.isParent) {
                            e.loading = false;
                            e.children = [];
                        }
                    });
                    let first = {
                        id: "0",
                        title: "一级部门"
                    };
                    res.result.unshift(first);
                    this.dataEdit = res.result;
                }
            });
        },
        loadData(item, callback) {
            loadDepartment(item.id).then(res => {
                if (res.success) {
                    res.result.forEach(function (e) {
                        if (e.isParent) {
                            e.loading = false;
                            e.children = [];
                            e._loading = false;
                        }
                    });
                    callback(res.result);
                }
            });
        },
        search() {
            if (this.searchKey) {
                this.loading = true;
                searchDepartment({
                    title: this.searchKey
                }).then(res => {
                    this.loading = false;
                    if (res.success) {
                        this.data = res.result;
                    }
                });
            } else {
                this.getParentList();
            }
        },
        excelData() {
            this.$refs.table.exportCsv({
                filename: "导出结果",
            });
        },
        selectTree(v) {
            if (v.length > 0) {
                for (let attr in v[0]) {
                    if (v[0][attr] == null) {
                        v[0][attr] = "";
                    }
                }
                let str = JSON.stringify(v[0]);
                let data = JSON.parse(str);
                this.editTitle = data.title;
                this.userLoading = true;
                getUserByDepartmentId(data.id).then(res => {
                    this.userLoading = false;
                    if (res.success) {
                        this.users = res.result;
                        this.form = data;
                    }
                });
                this.searchForm.departmentId = v[0].id;
                this.getUserDataList();
            } else {
                this.cancelEdit();
                this.searchForm.departmentId = "";
                this.getUserDataList();
            }
        },
        cancelEdit() {
            let data = this.$refs.tree.getSelectedNodes()[0];
            if (data) {
                data.selected = false;
            }
            this.$refs.form.resetFields();
            delete this.form.id;
            this.editTitle = "";
        },
        selectTreeEdit(v) {
            if (v.length > 0) {
                // 转换null为""
                for (let attr in v[0]) {
                    if (v[0][attr] == null) {
                        v[0][attr] = "";
                    }
                }
                let str = JSON.stringify(v[0]);
                let data = JSON.parse(str);
                this.form.parentId = data.id;
                this.form.parentTitle = data.title;
            }
        },
        cancelAdd() {
            this.modalVisible = false;
        },
        handleReset() {
            this.$refs.form.resetFields();
            this.form.status = 0;
        },
        showSelect(e) {
            this.selectList = e;
            this.selectCount = e.length;
        },
        clearSelectAll() {
            this.$refs.table.selectAll(false);
        },
        submitEdit() {
            this.$refs.form.validate(valid => {
                if (valid) {
                    if (!this.form.id) {
                        this.$Message.warning("请先点击选择要修改的部门");
                        return;
                    }
                    this.submitLoading = true;
                    editDepartment(this.form).then(res => {
                        this.submitLoading = false;
                        if (res.success) {
                            this.$Message.success("编辑成功");
                            this.init();
                            this.modalVisible = false;
                        }
                    });
                }
            });
        },
        submitAdd() {
            this.$refs.formAdd.validate(valid => {
                if (valid) {
                    this.submitLoading = true;
                    addDepartment(this.formAdd).then(res => {
                        this.submitLoading = false;
                        if (res.success) {
                            this.$Message.success("添加成功");
                            this.init();
                            this.modalVisible = false;
                        }
                    });
                }
            });
        },
        tableAdd(v) {
            this.form = v;
            this.add();
        },
        add() {
            if (this.form.id == "" || this.form.id == null) {
                this.$Message.warning("请先点击选择一个部门");
                return;
            }
            this.modalTitle = "添加子部门";
            this.showParent = true;
            this.formAdd = {
                parentId: this.form.id,
                sortOrder: 0,
                status: 0
            };
            this.modalVisible = true;
        },
        addRoot() {
            this.modalTitle = "添加一级部门";
            this.showParent = false;
            this.formAdd = {
                parentId: 0,
                sortOrder: 0,
                status: 0
            };
            this.modalVisible = true;
        },
        changeSelect(v) {
            this.selectCount = v.length;
            this.selectList = v;
        },
        remove(v) {
            this.selectCount = 1;
            this.selectList.push(v);
            this.delAll();
        },
        delAll() {
            if (this.selectCount <= 0) {
                this.$Message.warning("您还未勾选要删除的数据");
                return;
            }
            this.$Modal.confirm({
                title: "确认删除",
                content: "您确认要删除所选的 " + this.selectCount + " 条数据及其下级所有数据?",
                loading: true,
                onOk: () => {
                    let ids = "";
                    this.selectList.forEach(function (e) {
                        ids += e.id + ",";
                    });
                    ids = ids.substring(0, ids.length - 1);
                    deleteDepartment({
                        ids: ids
                    }).then(res => {
                        this.$Modal.remove();
                        if (res.success) {
                            this.$Message.success("删除成功");
                            this.selectList = [];
                            this.selectCount = 0;
                            this.cancelEdit();
                            this.init();
                        }
                    });
                }
            });
        }
    },
    mounted() {
        // 计算高度
        let height = document.documentElement.clientHeight;
        this.maxHeight = Number(height - 287) + "px";
        this.init();
    }
};
</script>
