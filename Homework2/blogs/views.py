from django.shortcuts import render, redirect
from django.http import HttpResponse, JsonResponse

from blogs.models import UserList,Poster


def login(request):
    return render(request, "login.html")


def login_verify(request):
    ctx ={}
    if request.method == 'POST':
        user = request.POST['username']
        pasw = request.POST['password']
        user_in_db = UserList.objects.filter(username=user).first()
        if user_in_db is None:
            return redirect(login)
        if user_in_db.username == user and pasw == user_in_db.password:
            ctx['username'] = user
            return render(request, "user.html", ctx)
        else:
            return HttpResponse("Error!")

def get_poster(request):
    ctx = {}
    result = []
    list = Poster.objects.all()
    for item in list:
        dic = {}
        dic['u1'] = item.userid
        dic['c1'] = item.postcontext
        result.append(dic)
    ctx['return'] = result
    return JsonResponse(ctx)


def register(request):
    return render(request, "register.html")



def register_verify(request):
    ctx = {}
    if request.method == 'POST':
        user = request.POST['username']
        pasw1 = request.POST['password1']
        pasw2 = request.POST['password2']
        if pasw1 != pasw2:
            return HttpResponse("两次密码输入的不相同")
        user_in_db = UserList.objects.filter(username=user).first()
        if user_in_db is not None:
            return HttpResponse("用户名已经被注册")
        test1 = UserList(username=user, password=pasw1)
        test1.save()
        return render(request,"login.html")