set -x
set -e
if [ ! -e hugo_0.17 ]; then
  wget https://github.com/spf13/hugo/releases/download/v0.17/hugo_0.17_Linux-64bit.tar.gz
  tar xvzf hugo_0.17_Linux-64bit.tar.gz
  mv hugo_0.17_linux_amd64 hugo_0.17
  mv hugo_0.17/hugo_0.17_linux_amd64 hugo_0.17/hugo
fi
