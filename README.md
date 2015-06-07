apache folder is needed to make a server to test with; it is not truly part of kitapp

I should maybe gitignore some generated files like .classpath and .project

INSTRUCTIONS:

Install EGit from eclipse marketplace. Preferably install eclipse EE, as it comes with a lot of plugins for web projects, maven, egit, etcetc.

Read https://wiki.eclipse.org/EGit/User_Guide until the 'Push upstream' section. Do the configurations described. DO NOT DO THE PUSH UPSTREAM SECTION.

Make new maven project. Team-> create repository -> git -> create local repository, normal stuff. Then, do team->remote->fetch. Use the https url for this project: https://github.com/pvutov/kitapp.git

Eclipse should now have downlaoded the repo and placed it in your project. You can commit it locally, make changes, and then push them with remote-> push.
