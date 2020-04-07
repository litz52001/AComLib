# AComLib
项目公共库
为方便多个项目同源引用该库，防止多个项目更新后版本冲突
以project作为lib引用，多个项目可以同时引用，同步更新

lib project修改
修改 lib库中的build.fradle
//apply plugin: 'com.android.application'为
apply plugin: 'com.android.library'
并把applicationId注释掉
//applicationId "com.acommonlibrary"

方法一：能引用修改不能同时
以module地址（../xx/xx/app）的形式正常导入Import Module作为引用即可，以module形式存在，在主项目下方

方法二：修改同时更新
在主项目下的settings.gradle文件添加
include ':BaseLibrary'
project(':BaseLibrary').projectDir = new File('../BaseLibrary')
include ':BaseLibrary:base' base为module名

主项目build.fradle添加引用
implementation project(path: ':BaseLibrary:base')
与主项目目录同级显示
compile(project(':qianwanli')) {
    exclude group: 'com.squareup.okhttp'
}
注意：
1、主项目虽然引用了BaseLibrary:base，但是却不能使用其所包含的第三方jar
    解决方法：
        修改BaseLibrary:base中的第三方jar依赖方式：
            将implementation改为api
            将testImplementation改为testApi
            将androidTestImplementation改为androidTestApi
            普通的jar包依赖冲突:
            compile(project(':qianwanli')) {
                exclude group: 'com.squareup.okhttp'
            }
2、当BaseLibrary项目中又增加了一个module命名为t_module，这个t_module引用了base之后，
    Test项目引用了t_module报错：
        Project with path ':base' could not be found in project ':BaseLibrary;t_module'
    解决方法：
        修改BaseLibrary:t_module的build.gradle
            implementation project(':base')
        改为
            implementation project(project.path.replace(project.name,'') +':base')

app更新库
https://github.com/azhon/AppUpdate
https://github.com/WVector/AppUpdate




