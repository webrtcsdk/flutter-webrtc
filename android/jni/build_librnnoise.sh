rm -rf build
mkdir -p build
cd build
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
cmake ..
make
cd ..