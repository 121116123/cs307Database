from django.urls import re_path as url
from blogs import views
urlpatterns = [
    url(r'^login$', views.login),
    url(r'^login_verify$', views.login_verify),
    url(r'^get_poster$', views.get_poster),
    url(r'^register$', views.register),
    url(r'^register_verify$', views.register_verify),
]