# xap-docs

This repo hosts XAP documentation, based on markdown and Hugo.

## Help Us Improve!

It's important for us to encourage your feedback and contribution. Contributing to this website is straightforward. Simply fork this repository, make your changes, test them with your local Hugo installation, and submit a pull request. We promise to review and comment on every pull request, and merge it if it makes sense.


# Installation

- Download [Hugo](https://github.com/spf13/hugo/releases) and unzip it
- Clone the [xap-docs](https://github.com/Gigaspaces/xap-docs) repository 

# Running

- Simply run `run.sh` (Linux) or `run.bat` (Windows)
- Browse to [localhost:1313](http://localhost:1313)

## (Optional) Navigation menu generation

The navigation menu is generated outside of Hugo via a script. The `run.sh` and `run.bat` scripts automatically genereate this menu by calling the `generate-navbar.sh` or `generate-navbar.bat` scripts before starting hugo. If you create a new page or change the weight of the pages, simply restart the script for the changes to take effect.


## (Optional) Making Changes to the Solutions & Best Practices Repo

If you want to edit a page in the solutions and best practices section:
The solutions and best practices section is located in a separate repository and is a submodule of this repository.  If you have push permissions to it, update the submodule as follows:

        git submodule init
        git submodule update
        cd  site/content/sbp
        git pull

Once you're done testing your changes in the `sbp` directory, check the changes in and push it seperately to the solutions and best practices repo:

        #cd to the sbp directory
        cd sbp

        # add the files you changed
        git add -A

        # commit your changes
        git commit -m "My changes"

        # push the changes to the solutions and best practices repo:
        git push




