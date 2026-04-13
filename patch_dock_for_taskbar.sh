#!/bin/bash
# First, change the apps_btn icon to the windows start icon in dock.xml
sed -i 's/@drawable\/ic_apps_menu/@drawable\/ic_windows_start/g' app/src/main/res/layout/dock.xml

# Change the apps_btn to trigger start menu.
# It seems apps_btn already triggers something. Let's see what it does.
