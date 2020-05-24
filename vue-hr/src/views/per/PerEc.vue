<template>
    <div>
        <div>
            <div style="display: flex;justify-content: space-between">
                <div>
                    <el-input placeholder="请输入员工名进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                              clearable
                              @clear="initEcs"
                              style="width: 350px;margin-right: 10px" v-model="keyword"
                              @keydown.enter.native="initEcs" :disabled="showAdvanceSearchView"></el-input>
                    <el-button icon="el-icon-search" type="primary" @click="initEcs" :disabled="showAdvanceSearchView">
                        搜索
                    </el-button>
                     <el-button type="primary" @click="showAdvanceSearchView = !showAdvanceSearchView">
                        <i :class="showAdvanceSearchView?'fa fa-angle-double-up':'fa fa-angle-double-down'"
                           aria-hidden="true"></i>
                        高级搜索
                    </el-button>
                </div>
                <div>
                    <el-button type="primary" icon="el-icon-plus" @click="showAddEcView">
                        添加用户
                    </el-button>
                </div>
            </div>
            <transition name="slide-fade">
                <div v-show="showAdvanceSearchView"
                     style="border: 1px solid #409eff;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0px;">
                    <el-row>
                        <el-col :span="5">
                            部门编号:
                            <el-select v-model="searchValue.sectorId" placeholder="部门编号" size="mini"
                                       style="width: 130px;">
                                <el-option label="201" value="201"></el-option>
                                <el-option label="202" value="202"></el-option>
                                <el-option label="203" value="203"></el-option>
                                <el-option label="204" value="204"></el-option>
                                <el-option label="205" value="205"></el-option>
                                <el-option label="206" value="206"></el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="4">
                            部门名称:
                            <el-select v-model="searchValue.sectorName" placeholder="部门名称" size="mini"
                                       style="width: 130px;">
                                <el-option
                                        v-for="item in sector"
                                        :key="item.name"
                                        :label="item.name"
                                        :value="item.name">
                                </el-option>
                            </el-select>
                        </el-col>
                       
                        <el-col :span="5" :offset="4" style="margin-left:400px;">
                            <el-button size="mini" @click="dialogVisible = false">取消</el-button>
                            <el-button size="mini" icon="el-icon-search" type="primary" @click="initEcs('advanced')">搜索</el-button>
                        </el-col>
                    </el-row>
                </div>
            </transition>
        </div>
        <div style="margin-top: 10px">
            <el-table
                    :data="ecs"
                    stripe
                    border
                    v-loading="loading"
                    element-loading-text="正在加载..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    style="width: 100%">
                <el-table-column
                        type="selection"
                        align="center"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="name"
                        fixed
                        align="center"
                        label="姓名"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="workId"
                        label="员工编号"
                        align="center"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="sectorId"
                        label="部门编号"
                        align="center"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="sector.name"
                        width="100"
                        align="center"
                        label="部门名称">
                </el-table-column>
                <el-table-column
                        prop="thing"
                        width="300"
                        align="center"
                        label="奖惩事项">
                </el-table-column>
                <el-table-column
                        prop="action"
                        width="200"
                        align="center"
                        label="奖惩办法">
                </el-table-column>
                <el-table-column
                        prop="warning"
                        width="100"
                        align="center"
                        label="警告">
                </el-table-column>
                <el-table-column
                        prop="demerit"
                        width="100"
                        align="center"
                        label="记过">
                </el-table-column>
                <el-table-column
                        prop="cite"
                        width="100"
                        align="center"
                        label="嘉奖">
                </el-table-column>
                <el-table-column
                        prop="merit"
                        width="100"
                        align="center"
                        label="记功">
                </el-table-column>
                <el-table-column
                        prop="bigMerit"
                        width="100"
                        align="center"
                        label="记大功">
                </el-table-column>
                <el-table-column
                        align="center"
                        width="200"
                        label="操作">
                    <template slot-scope="scope">
                        <el-button @click="showEditEcView(scope.row)" style="padding: 3px" size="mini">编辑</el-button>
                        <el-button @click="deleteEc(scope.row)" style="padding: 3px" size="mini" type="danger">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div style="display: flex;justify-content: flex-end">
                <el-pagination
                        background
                        @current-change="currentChange"
                        @size-change="sizeChange"
                        layout="sizes, prev, pager, next, jumper, ->, total, slot"
                        :total="total">
                </el-pagination>
            </div>
        </div>
        <el-dialog
                :title="title"
                :visible.sync="dialogVisible"
                width="80%">
             <div>
                <el-form :model="ec" :rules="rules" ref="ecForm">
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="姓名:" prop="name">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit" v-model="ec.name"
                                          placeholder="请输入员工姓名"></el-input>
                            </el-form-item>
                        </el-col>
                         <el-col :span="6">
                            <el-form-item label="员工编号:" prop="workId">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                          v-model="ec.workId" placeholder="员工编号" ></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="部门编号:" prop="sectorId">
                                 <el-select v-model="ec.sectorId" placeholder="部门编号" size="mini" style="width: 150px;">
                                    <el-option label="201" value="201"></el-option>
                                    <el-option label="202" value="202"></el-option>
                                    <el-option label="203" value="203"></el-option>
                                    <el-option label="204" value="204"></el-option>
                                    <el-option label="205" value="205"></el-option>
                                    <el-option label="206" value="206"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="部门名称:" prop="sectorName">
                               <el-select v-model="ec.sectorName" placeholder="部门名称" size="mini" style="width: 150px;">
                                    <el-option
                                            v-for="item in sector"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        
                    </el-row>
                    <el-row>
                        <el-col :span="7">
                            <el-form-item label="奖惩事项:" prop="thing">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit" v-model="ec.thing"
                                          placeholder="请输入奖惩事项"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="奖惩办法:" prop="action">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit" v-model="ec.action"
                                          placeholder="请输入奖惩办法"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                                 <el-form-item label="警告:" prop="warning">
                                <el-radio-group v-model="ec.warning">
                                    <el-radio label="是">是</el-radio>
                                    <el-radio label="否">否</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                                 <el-form-item label="记过:" prop="demerit">
                                <el-radio-group v-model="ec.demerit">
                                    <el-radio label="是">是</el-radio>
                                    <el-radio label="否">否</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                         
                    </el-row>
                    <el-row>
                        <el-col :span="5">
                                 <el-form-item label="记大过:" prop="bigDemerit">
                                <el-radio-group v-model="ec.bigDemerit">
                                    <el-radio label="是">是</el-radio>
                                    <el-radio label="否">否</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                                 <el-form-item label="嘉奖:" prop="cite">
                                <el-radio-group v-model="ec.cite">
                                    <el-radio label="是">是</el-radio>
                                    <el-radio label="否">否</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                                 <el-form-item label="记功:" prop="merit">
                                <el-radio-group v-model="ec.merit">
                                    <el-radio label="是">是</el-radio>
                                    <el-radio label="否">否</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                                 <el-form-item label="记大功:" prop="bigMerit">
                                <el-radio-group v-model="ec.bigMerit">
                                    <el-radio label="是">是</el-radio>
                                    <el-radio label="否">否</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        
                    </el-row>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="doAddEc">确 定</el-button>
  </span>
        </el-dialog>
    </div>
</template>
<script>
    export default {
        name: "PerEc",
        data() {
            return {
                searchValue: {
                    workId: null,
                    sectorId: null,
                    sectorName: null,
                    thing: null,
                    action: null
                },
                title: '',
                 sector: [],
                ecs: [],
                loading: false,
                dialogVisible: false,
                showAdvanceSearchView: false,
                total: 0,
                page: 1,
                keyword: '',
                size: 10,      
                ec: {
                    name: '孙千喜',
                    workId: 10001,
                    sectorId: 201,
                    sectorName: 1,
                    thing: '成本管理取得重大成就',
                    action: '奖金2000元',
                    warning: '否',
                    demerit: '否',
                    bigDemerit: '否',
                    cite: '是',
                    merit: '否',
                    bigMerit: '否'
                },
                rules: {
                    name: [{required: true, message: '请输入员工姓名', trigger: 'blur'}],
                    sectorId: [{required: true, message: '请输入部门编号', trigger: 'blur'}],
                    sectorName: [{required: true, message: '请输入部门名称', trigger: 'blur'}],
                    thing: [{required: true, message: '请输入奖惩事项', trigger: 'blur'}],
                    action: [{required: true, message: '请输入奖惩办法', trigger: 'blur'}],
                    warning: [{required: true, message: '是否警告', trigger: 'blur'}],
                    demerit: [{required: true, message: '是否记过', trigger: 'blur'}],
                    bigDemerit: [{required: true, message: '是否记大过', trigger: 'blur' }],
                    cite: [{required: true, message: '是否嘉奖', trigger: 'blur'}],
                    merit: [{required: true, message: '是否记功', trigger: 'blur'}],
                    bigMerit: [{required: true, message: '是否记大功', trigger: 'blur'}],
                    workId: [{required: true, message: '请输入员工编号', trigger: 'blur'}],
                }
            }
        },
        mounted() {
            this.initEcs();
            this.initData();
        },
        methods: {
            emptyEc() {
                this.ec = {
                    name: "",
                    workId: "",
                    sectorId: "",
                    sectorName: "",
                    thing: "",
                    action: "",
                    warning: "",
                    demerit: "",
                    bigDemerit: "",
                    cite: "",
                    merit: "",
                    bigMerit: ""
                }
                this.inputDepName = '';
            },
            initData() {
                 if (!window.sessionStorage.getItem("sector")) {
                    this.getRequest('/personnel/ec/sector').then(resp => {
                        if (resp) {
                            this.sector = resp;
                            window.sessionStorage.setItem("sector", JSON.stringify(resp));
                        }
                    })
                } else {
                    this.sector = JSON.parse(window.sessionStorage.getItem("sector"));
                }
             },
            showEditEcView(data) {
                this.initEcs();
                this.title = '编辑员工信息';
                this.ec = data;
                // this.inputDepName = data.ec.name;
                this.dialogVisible = true;
            },
            deleteEc(data) {
                this.$confirm('此操作将永久删除【' + data.name + '】, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/personnel/ec/" + data.id).then(resp => {
                        if (resp) {
                            this.initEcs();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            doAddEc() {
                if (this.ec.id) {
                    this.$refs['ecForm'].validate(valid => {
                        if (valid) {
                            this.putRequest("/personnel/ec/", this.ec).then(resp => {
                                if (resp) {
                                    this.dialogVisible = false;
                                    this.initEcs();
                                }
                            })
                        }
                    });
                } else {
                    this.$refs['ecForm'].validate(valid => {
                        if (valid) {
                            this.postRequest("/personnel/ec/", this.ec).then(resp => {
                                if (resp) {
                                    this.dialogVisible = false;
                                    this.initEcs();
                                }
                            })
                        }
                    });
                }
            },
            sizeChange(currentSize) {
                this.size = currentSize;
                this.initEcs();
            },
            currentChange(currentPage) {
                this.page = currentPage;
                this.initEcs();
            },
            showAddEcView() {
                this.emptyEc();
                this.title = '添加员工';
                this.dialogVisible = true;
            },
            initEcs(type) {
                this.loading = true;
                let url = '/personnel/ec/?page=' + this.page + '&size=' + this.size;
                 if (type && type == 'advanced') {
                    if (this.searchValue.workId) {
                        url += '&workId=' + this.searchValue.workId;
                    }
                    if (this.searchValue.sectorId) {
                        url += '&sectorId=' + this.searchValue.sectorId;
                    }
                    if (this.searchValue.sectorName) {
                        url += '&sectorName=' + this.searchValue.sectorName;
                    }
                    if (this.searchValue.thing) {
                        url += '&thing=' + this.searchValue.thing;
                    }
                    if (this.searchValue.action) {
                        url += '&action=' + this.searchValue.action;
                    }
                }  else {
                    url += "&name=" + this.keyword;
                }
                this.getRequest(url).then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.ecs = resp.data;
                        this.total = resp.total;
                    }
                });
            }
        }
    }
</script>
<style>
    /* 可以设置不同的进入和离开动画 */
    /* 设置持续时间和动画函数 */
    .slide-fade-enter-active {
        transition: all .8s ease;
    }
    .slide-fade-leave-active {
        transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
    }
    .slide-fade-enter, .slide-fade-leave-to
        /* .slide-fade-leave-active for below version 2.1.8 */
    {
        transform: translateX(10px);
        opacity: 0;
    }
</style>