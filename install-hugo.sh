set -x
set -e
if [ ! -e hugo ]; then
  wget https://github.com/spf13/hugo/releases/download/v0.14/hugo_0.14_linux_amd64.tar.gz
  tar xvzf hugo_0.14_linux_amd64.tar.gz
  mv hugo_0.14_linux_amd64 hugo
  mv hugo/hugo_0.14_linux_amd64 hugo/hugo
fi