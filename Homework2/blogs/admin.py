from django.contrib import admin

# Register your models here.
from blogs import models
admin.site.register(models.UserList)

admin.site.register(models.Poster)