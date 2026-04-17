1、项目必须启动redis服务
2、项目必须配置文件中配置三个参数：
    1).fileSavePath：附件保存路径(必须以/结尾)，例如：fileSavePath=/home/task/
    2).restartCount：接口重调上限(正整数)，例如：restartCount=3
    3).applicationPath：yml配置绝对路径，例如：applicationPath=I:/Intellij IDEA-workpace/application.yml
3、项目启动类必须加定时器注解(启动定时器)、包扫描注解(扫描xml和@Component):
    1).@EnableScheduling //开启定时器
    2).@MapperScan({"xxxx自己项目扫码包","org.dockingProjects.mapper"})
    3).@ComponentScan({"xxxx自己项目扫码包","org.dockingProjects"})
    
4、需要将次项目的application.yml单独配置，在引入的项目如添加路径