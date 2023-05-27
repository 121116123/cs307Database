# -*- coding: utf-8 -*-
import jpype
import os

if __name__ == '__main__':
    """
    基本的开发流程如下：
    ①、使用jpype开启jvm
    ②、加载java类
    ③、调用java方法
    ④、关闭jvm（不是真正意义上的关闭，卸载之前加载的类）
    """

    # ①、使用jpype开启虚拟机（在开启jvm之前要加载类路径）
    # 加载刚才打包的jar文件
    jarpath = os.path.join(os.path.abspath('.'), 'D:\AAAA\study\cs307\cs307_7\project2.jar')

    # 获取jvm.dll的文件路径
    jvmPath = jpype.getDefaultJVMPath()

    # 开启jvm
    jpype.startJVM(jvmPath, '-ea', '-Djava.class.path=%s' % (jarpath))
    # 加载java类（参数名是java的长类名）
    javaClass = jpype.JClass('Main')

    # 实例化java对象
    args=[]
    javaInstance = javaClass()
    javaClass.show(args)
    # javaClass.author_favorite_share_like_post("mx")
    print(javaClass.authors)
# 关闭jvm
jpype.shutdownJVM()
