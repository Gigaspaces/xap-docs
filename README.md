# xap-docs
xxxxx

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

