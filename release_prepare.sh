#GIT FLOW VERSION
#git flow release start 1.5.2
#mvn release:prepare -DreleaseVersion=1.5.2 -Dtag=1.5.2 -DdevelopmentVersion=1.5.3-SNAPSHOT --batch-mode -DdryRun=false -Drelease
#mvn release:perform -Drelease
#git flow release finish 1.5.2
#git push origin master develop :release/1.5.2

#STANDARD VERSION
git checkout develop
git checkout -b release/1.5.2
mvn release:prepare -DreleaseVersion=1.5.2 -Dtag=1.5.2 -DdevelopmentVersion=1.5.3-SNAPSHOT --batch-mode -DdryRun=false -Drelease
mvn release:perform -Drelease
# merge the version changes back into develop so that folks are working against 
#	the new release ("0.0.3-SNAPSHOT", in this case)
git checkout develop
git merge --no-ff release/1.5.2

# housekeeping -- rewind the release branch by one commit to fix its version at "0.0.2"
#	excuse the force push, it's because maven will have already pushed '0.0.3-SNAPSHOT'
#	to origin with this branch, and I don't want that version (or a diverging revert commit)
#	in the release or master branches.
git checkout release/1.5.2
git reset --hard HEAD~1
git push --force origin release/1.5.2
git checkout develop

# finally, if & when the code gets deployed to production
git checkout master
git merge --no-ff release/1.5.2
git branch -d release/1.5.2
git push