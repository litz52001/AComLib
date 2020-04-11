# AComLib
项目公共库 推荐使用Typora阅读
为方便多个项目同源引用该库，防止多个项目更新后版本冲突
以project作为lib引用，多个项目可以同时引用，同步更新

lib project修改
修改 lib库中的build.fradle
//apply plugin: 'com.android.application'为
apply plugin: 'com.android.library'
并把applicationId注释掉
//applicationId "com.acommonlibrary"

AndroidManifest.xml中的application删除不然会出现2个图标

方法一：能引用修改不能同步
以module地址（../xx/xx/app）的形式正常导入Import Module作为引用即可，以module形式存在，在主项目下方

方法二：修改同时更新（推荐）
在主项目下的settings.gradle文件添加
include ':BaseLibrary'
project(':BaseLibrary').projectDir = new File('../BaseLibrary/app')//app为module名
与主项目目录同级显示；lib地址最好是同磁盘下引用
**可根据不同项目需要创建多个module作为引用避免不必要的引用导致打包过大**

主项目build.fradle添加引用
implementation project(':BaseLibrary')
注意：
1、主项目虽然引用了BaseLibrary，但是却不能使用其所包含的第三方jar
    解决方法：
        修改BaseLibrary:base中的第三方jar依赖方式：
            将implementation改为api
            将testImplementation改为testApi
            将androidTestImplementation改为androidTestApi
            普通的jar包依赖冲突:
            compile(project(':qianwanli')) {
                exclude group: 'com.squareup.okhttp'
            }

###### app更新库
https://github.com/azhon/AppUpdate
https://github.com/WVector/AppUpdate

###### 无法执行java main方法
在新项目的 .idea/gradle.xml下<GradleProjectSettings>节点添加
`<option name="delegatedBuild" value="false" />`
添加后如果出现找不到或无法加载主类重启即可




