
//页面加载完后执行function代码
$(function () {
    //jquery 使用$("#id")通过元素id获取某个页面元素
    $("#login_form").submit(function () {
        //ajax发请求
        $.ajax({
            url:"../login",//请求的服务路径
            type:"post",//请求方法
            //contentType:"",//请求的数据类型：请求头content—type，默认表单格式，json
            data:$("#login_form").serialize(),//请求数据:序列化表单的数据
            dataType:"json" ,//响应的的数据类型：json需要手动指定
            success:function (r) {//响应体json字符串，会解析为方法参数
                if(r.success){
                    //前端页面url直接跳转到某个路径
                    window.location.href="../jsp/articleList.jsp";
                }else {
                    alert("错误码："+r.code+"\n错误消息："+r.message)
                }
            }
        })

        //统一不执行默认的表单提交
        return false;
    })
})