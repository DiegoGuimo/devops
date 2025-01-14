# Asegúrate de estar en el repositorio correcto
git checkout master
git pull origin master

git checkout feature
git pull origin feature
git merge master

git checkout master
git merge feature

git push origin master
git push origin feature