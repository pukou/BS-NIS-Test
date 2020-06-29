RPC.jar

本说明文件作用：说明rpc.jar的依赖关系、核心功能、发布RPC服务步骤
依赖关系：
    BS-NIS-CoreApi.jar  核心接口包，提供接口支持
    BS-NIS-Bean.jar     Java对象包，提供对象支持
    BS-NIS-*.jar        业务服务,为RPC提供业务服务支持（除了BS-NIS不可引用外，其他业务模块服务都可以引用）

核心功能：对外提供hessian服务
    ①对外接口实现，完成业务数据组装
    ②对外发布RPC服务，hessian协议
    ③

发布服务步骤：
    ①在BS-NIS-CoreApi模块中增加服务接口，如添加 test.java (接口类型)
    ②在BS-NIS-RPC模块中增加test.java的实现类,且调用核心服务的完成服务的功能
    ③在hessian-patient.xml中，发布服务