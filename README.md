# Empty Liferay portlet.

Use as base for test/poc/debug portlets.

To use this as basis for a new portlet project:
- make a clone
- rename the cloned dir to new-name
- cd into it
- rm -rf .git/
- sed -i 's/download-portlet/new-name/g' \`grep -rl download-portlet *\`
- git init .
- etc.

(there are probably better ways to do this...)

Note : not all Maven dependecies are free (Primefaces, Liferay EE)
