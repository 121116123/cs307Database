from django.db import models

# Create your models here.
class UserList(models.Model):
    userid = models.AutoField(db_column='user_id',primary_key=True)
    username = models.CharField(max_length=32)
    password = models.CharField(max_length=32)



class Poster(models.Model):
    postid = models.AutoField(db_column='post_id',primary_key=True)
    userid = models.IntegerField()
    postcontext = models.CharField(max_length=512)

'''
python manage.py makemigrations
python manage.py migrate
'''