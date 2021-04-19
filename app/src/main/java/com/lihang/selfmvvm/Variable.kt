package com.lihang.selfmvvm

import com.lihang.selfmvvm.bean.ChildModel
import com.lihang.selfmvvm.vo.res.MemberDetailResVo
import java.util.ArrayList

/**
 * 全局变量
 */
class Variable {
    companion object {
        @JvmField
        var userList:List<ChildModel> = ArrayList()

        @JvmField
        var govermentGroupUserList:List<MemberDetailResVo> = ArrayList()
        @JvmField
        var enterpriseGroupUserList:List<MemberDetailResVo> = ArrayList()
    }
}
