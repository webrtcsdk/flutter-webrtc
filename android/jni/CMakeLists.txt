# Định nghĩa biến cho đường dẫn của NDK
set(NDK_PATH /Users/lambiengcode/Library/Android/sdk/ndk/24.0.8215888)

# Set the minimum required CMake version
cmake_minimum_required(VERSION 3.18)

set(CMAKE_CXX_FLAGS "-std=c++17 -DANDROID -g")

# Set the name of your project
project(rnnoise)

# Add the source files for rnnoise
add_library(rnnoise SHARED
        rnnoise/src/celt_lpc.c
        rnnoise/src/denoise.c
        rnnoise/src/kiss_fft.c
        rnnoise/src/pitch.c
        rnnoise/src/rnn_data.c
        rnnoise/src/rnn_reader.c
        rnnoise/src/rnn.c
        rnnoise_wrapper.c)

# Set compiler flags if needed
target_compile_options(rnnoise PUBLIC
        -Wall -finline-functions -fno-strict-aliasing -O3 -frtti -Wno-unknown-pragmas -funroll-loops -fexceptions -fno-math-errno)

# Set target properties
set_target_properties(rnnoise PROPERTIES
    ANDROID_ARM_MODE arm
    OUTPUT_NAME "rnnoise"
    SUFFIX ".so")

# Define compile definitions
target_compile_definitions(rnnoise PRIVATE
        # Add your compile definitions here
)

# Set include directories, including the NDK path
target_include_directories(rnnoise PUBLIC
        rnnoise/include
        ${NDK_PATH}/sysroot/usr/include)

# Specify where to place the built library
set_target_properties(rnnoise PROPERTIES
        ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_SOURCE_DIR}/jniLibs/${ANDROID_ABI})
