package com.lihang.selfmvvm

import com.lihang.selfmvvm.bean.ChildModel
import com.lihang.selfmvvm.vo.res.MemberDetailResVo
import java.util.ArrayList

/**
 * ćšć±ćé
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
