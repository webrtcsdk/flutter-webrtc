# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.27

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Disable VCS-based implicit rules.
% : %,v

# Disable VCS-based implicit rules.
% : RCS/%

# Disable VCS-based implicit rules.
% : RCS/%,v

# Disable VCS-based implicit rules.
% : SCCS/s.%

# Disable VCS-based implicit rules.
% : s.%

.SUFFIXES: .hpux_make_needs_suffix_list

# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

#Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/local/Cellar/cmake/3.27.0/bin/cmake

# The command to remove a file.
RM = /usr/local/Cellar/cmake/3.27.0/bin/cmake -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni

# Include any dependencies generated for this target.
include CMakeFiles/rnnoise.dir/depend.make
# Include any dependencies generated by the compiler for this target.
include CMakeFiles/rnnoise.dir/compiler_depend.make

# Include the progress variables for this target.
include CMakeFiles/rnnoise.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/rnnoise.dir/flags.make

CMakeFiles/rnnoise.dir/rnnoise/src/celt_lpc.c.o: CMakeFiles/rnnoise.dir/flags.make
CMakeFiles/rnnoise.dir/rnnoise/src/celt_lpc.c.o: rnnoise/src/celt_lpc.c
CMakeFiles/rnnoise.dir/rnnoise/src/celt_lpc.c.o: CMakeFiles/rnnoise.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --progress-dir=/Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/rnnoise.dir/rnnoise/src/celt_lpc.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -MD -MT CMakeFiles/rnnoise.dir/rnnoise/src/celt_lpc.c.o -MF CMakeFiles/rnnoise.dir/rnnoise/src/celt_lpc.c.o.d -o CMakeFiles/rnnoise.dir/rnnoise/src/celt_lpc.c.o -c /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/celt_lpc.c

CMakeFiles/rnnoise.dir/rnnoise/src/celt_lpc.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Preprocessing C source to CMakeFiles/rnnoise.dir/rnnoise/src/celt_lpc.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/celt_lpc.c > CMakeFiles/rnnoise.dir/rnnoise/src/celt_lpc.c.i

CMakeFiles/rnnoise.dir/rnnoise/src/celt_lpc.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Compiling C source to assembly CMakeFiles/rnnoise.dir/rnnoise/src/celt_lpc.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/celt_lpc.c -o CMakeFiles/rnnoise.dir/rnnoise/src/celt_lpc.c.s

CMakeFiles/rnnoise.dir/rnnoise/src/denoise.c.o: CMakeFiles/rnnoise.dir/flags.make
CMakeFiles/rnnoise.dir/rnnoise/src/denoise.c.o: rnnoise/src/denoise.c
CMakeFiles/rnnoise.dir/rnnoise/src/denoise.c.o: CMakeFiles/rnnoise.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --progress-dir=/Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building C object CMakeFiles/rnnoise.dir/rnnoise/src/denoise.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -MD -MT CMakeFiles/rnnoise.dir/rnnoise/src/denoise.c.o -MF CMakeFiles/rnnoise.dir/rnnoise/src/denoise.c.o.d -o CMakeFiles/rnnoise.dir/rnnoise/src/denoise.c.o -c /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/denoise.c

CMakeFiles/rnnoise.dir/rnnoise/src/denoise.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Preprocessing C source to CMakeFiles/rnnoise.dir/rnnoise/src/denoise.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/denoise.c > CMakeFiles/rnnoise.dir/rnnoise/src/denoise.c.i

CMakeFiles/rnnoise.dir/rnnoise/src/denoise.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Compiling C source to assembly CMakeFiles/rnnoise.dir/rnnoise/src/denoise.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/denoise.c -o CMakeFiles/rnnoise.dir/rnnoise/src/denoise.c.s

CMakeFiles/rnnoise.dir/rnnoise/src/kiss_fft.c.o: CMakeFiles/rnnoise.dir/flags.make
CMakeFiles/rnnoise.dir/rnnoise/src/kiss_fft.c.o: rnnoise/src/kiss_fft.c
CMakeFiles/rnnoise.dir/rnnoise/src/kiss_fft.c.o: CMakeFiles/rnnoise.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --progress-dir=/Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Building C object CMakeFiles/rnnoise.dir/rnnoise/src/kiss_fft.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -MD -MT CMakeFiles/rnnoise.dir/rnnoise/src/kiss_fft.c.o -MF CMakeFiles/rnnoise.dir/rnnoise/src/kiss_fft.c.o.d -o CMakeFiles/rnnoise.dir/rnnoise/src/kiss_fft.c.o -c /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/kiss_fft.c

CMakeFiles/rnnoise.dir/rnnoise/src/kiss_fft.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Preprocessing C source to CMakeFiles/rnnoise.dir/rnnoise/src/kiss_fft.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/kiss_fft.c > CMakeFiles/rnnoise.dir/rnnoise/src/kiss_fft.c.i

CMakeFiles/rnnoise.dir/rnnoise/src/kiss_fft.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Compiling C source to assembly CMakeFiles/rnnoise.dir/rnnoise/src/kiss_fft.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/kiss_fft.c -o CMakeFiles/rnnoise.dir/rnnoise/src/kiss_fft.c.s

CMakeFiles/rnnoise.dir/rnnoise/src/pitch.c.o: CMakeFiles/rnnoise.dir/flags.make
CMakeFiles/rnnoise.dir/rnnoise/src/pitch.c.o: rnnoise/src/pitch.c
CMakeFiles/rnnoise.dir/rnnoise/src/pitch.c.o: CMakeFiles/rnnoise.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --progress-dir=/Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/CMakeFiles --progress-num=$(CMAKE_PROGRESS_4) "Building C object CMakeFiles/rnnoise.dir/rnnoise/src/pitch.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -MD -MT CMakeFiles/rnnoise.dir/rnnoise/src/pitch.c.o -MF CMakeFiles/rnnoise.dir/rnnoise/src/pitch.c.o.d -o CMakeFiles/rnnoise.dir/rnnoise/src/pitch.c.o -c /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/pitch.c

CMakeFiles/rnnoise.dir/rnnoise/src/pitch.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Preprocessing C source to CMakeFiles/rnnoise.dir/rnnoise/src/pitch.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/pitch.c > CMakeFiles/rnnoise.dir/rnnoise/src/pitch.c.i

CMakeFiles/rnnoise.dir/rnnoise/src/pitch.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Compiling C source to assembly CMakeFiles/rnnoise.dir/rnnoise/src/pitch.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/pitch.c -o CMakeFiles/rnnoise.dir/rnnoise/src/pitch.c.s

CMakeFiles/rnnoise.dir/rnnoise/src/rnn_data.c.o: CMakeFiles/rnnoise.dir/flags.make
CMakeFiles/rnnoise.dir/rnnoise/src/rnn_data.c.o: rnnoise/src/rnn_data.c
CMakeFiles/rnnoise.dir/rnnoise/src/rnn_data.c.o: CMakeFiles/rnnoise.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --progress-dir=/Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/CMakeFiles --progress-num=$(CMAKE_PROGRESS_5) "Building C object CMakeFiles/rnnoise.dir/rnnoise/src/rnn_data.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -MD -MT CMakeFiles/rnnoise.dir/rnnoise/src/rnn_data.c.o -MF CMakeFiles/rnnoise.dir/rnnoise/src/rnn_data.c.o.d -o CMakeFiles/rnnoise.dir/rnnoise/src/rnn_data.c.o -c /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/rnn_data.c

CMakeFiles/rnnoise.dir/rnnoise/src/rnn_data.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Preprocessing C source to CMakeFiles/rnnoise.dir/rnnoise/src/rnn_data.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/rnn_data.c > CMakeFiles/rnnoise.dir/rnnoise/src/rnn_data.c.i

CMakeFiles/rnnoise.dir/rnnoise/src/rnn_data.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Compiling C source to assembly CMakeFiles/rnnoise.dir/rnnoise/src/rnn_data.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/rnn_data.c -o CMakeFiles/rnnoise.dir/rnnoise/src/rnn_data.c.s

CMakeFiles/rnnoise.dir/rnnoise/src/rnn_reader.c.o: CMakeFiles/rnnoise.dir/flags.make
CMakeFiles/rnnoise.dir/rnnoise/src/rnn_reader.c.o: rnnoise/src/rnn_reader.c
CMakeFiles/rnnoise.dir/rnnoise/src/rnn_reader.c.o: CMakeFiles/rnnoise.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --progress-dir=/Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/CMakeFiles --progress-num=$(CMAKE_PROGRESS_6) "Building C object CMakeFiles/rnnoise.dir/rnnoise/src/rnn_reader.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -MD -MT CMakeFiles/rnnoise.dir/rnnoise/src/rnn_reader.c.o -MF CMakeFiles/rnnoise.dir/rnnoise/src/rnn_reader.c.o.d -o CMakeFiles/rnnoise.dir/rnnoise/src/rnn_reader.c.o -c /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/rnn_reader.c

CMakeFiles/rnnoise.dir/rnnoise/src/rnn_reader.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Preprocessing C source to CMakeFiles/rnnoise.dir/rnnoise/src/rnn_reader.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/rnn_reader.c > CMakeFiles/rnnoise.dir/rnnoise/src/rnn_reader.c.i

CMakeFiles/rnnoise.dir/rnnoise/src/rnn_reader.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Compiling C source to assembly CMakeFiles/rnnoise.dir/rnnoise/src/rnn_reader.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/rnn_reader.c -o CMakeFiles/rnnoise.dir/rnnoise/src/rnn_reader.c.s

CMakeFiles/rnnoise.dir/rnnoise/src/rnn.c.o: CMakeFiles/rnnoise.dir/flags.make
CMakeFiles/rnnoise.dir/rnnoise/src/rnn.c.o: rnnoise/src/rnn.c
CMakeFiles/rnnoise.dir/rnnoise/src/rnn.c.o: CMakeFiles/rnnoise.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --progress-dir=/Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/CMakeFiles --progress-num=$(CMAKE_PROGRESS_7) "Building C object CMakeFiles/rnnoise.dir/rnnoise/src/rnn.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -MD -MT CMakeFiles/rnnoise.dir/rnnoise/src/rnn.c.o -MF CMakeFiles/rnnoise.dir/rnnoise/src/rnn.c.o.d -o CMakeFiles/rnnoise.dir/rnnoise/src/rnn.c.o -c /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/rnn.c

CMakeFiles/rnnoise.dir/rnnoise/src/rnn.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Preprocessing C source to CMakeFiles/rnnoise.dir/rnnoise/src/rnn.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/rnn.c > CMakeFiles/rnnoise.dir/rnnoise/src/rnn.c.i

CMakeFiles/rnnoise.dir/rnnoise/src/rnn.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Compiling C source to assembly CMakeFiles/rnnoise.dir/rnnoise/src/rnn.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise/src/rnn.c -o CMakeFiles/rnnoise.dir/rnnoise/src/rnn.c.s

CMakeFiles/rnnoise.dir/rnnoise_wrapper.c.o: CMakeFiles/rnnoise.dir/flags.make
CMakeFiles/rnnoise.dir/rnnoise_wrapper.c.o: rnnoise_wrapper.c
CMakeFiles/rnnoise.dir/rnnoise_wrapper.c.o: CMakeFiles/rnnoise.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --progress-dir=/Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/CMakeFiles --progress-num=$(CMAKE_PROGRESS_8) "Building C object CMakeFiles/rnnoise.dir/rnnoise_wrapper.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -MD -MT CMakeFiles/rnnoise.dir/rnnoise_wrapper.c.o -MF CMakeFiles/rnnoise.dir/rnnoise_wrapper.c.o.d -o CMakeFiles/rnnoise.dir/rnnoise_wrapper.c.o -c /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise_wrapper.c

CMakeFiles/rnnoise.dir/rnnoise_wrapper.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Preprocessing C source to CMakeFiles/rnnoise.dir/rnnoise_wrapper.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise_wrapper.c > CMakeFiles/rnnoise.dir/rnnoise_wrapper.c.i

CMakeFiles/rnnoise.dir/rnnoise_wrapper.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Compiling C source to assembly CMakeFiles/rnnoise.dir/rnnoise_wrapper.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/rnnoise_wrapper.c -o CMakeFiles/rnnoise.dir/rnnoise_wrapper.c.s

# Object files for target rnnoise
rnnoise_OBJECTS = \
"CMakeFiles/rnnoise.dir/rnnoise/src/celt_lpc.c.o" \
"CMakeFiles/rnnoise.dir/rnnoise/src/denoise.c.o" \
"CMakeFiles/rnnoise.dir/rnnoise/src/kiss_fft.c.o" \
"CMakeFiles/rnnoise.dir/rnnoise/src/pitch.c.o" \
"CMakeFiles/rnnoise.dir/rnnoise/src/rnn_data.c.o" \
"CMakeFiles/rnnoise.dir/rnnoise/src/rnn_reader.c.o" \
"CMakeFiles/rnnoise.dir/rnnoise/src/rnn.c.o" \
"CMakeFiles/rnnoise.dir/rnnoise_wrapper.c.o"

# External object files for target rnnoise
rnnoise_EXTERNAL_OBJECTS =

librnnoise.so: CMakeFiles/rnnoise.dir/rnnoise/src/celt_lpc.c.o
librnnoise.so: CMakeFiles/rnnoise.dir/rnnoise/src/denoise.c.o
librnnoise.so: CMakeFiles/rnnoise.dir/rnnoise/src/kiss_fft.c.o
librnnoise.so: CMakeFiles/rnnoise.dir/rnnoise/src/pitch.c.o
librnnoise.so: CMakeFiles/rnnoise.dir/rnnoise/src/rnn_data.c.o
librnnoise.so: CMakeFiles/rnnoise.dir/rnnoise/src/rnn_reader.c.o
librnnoise.so: CMakeFiles/rnnoise.dir/rnnoise/src/rnn.c.o
librnnoise.so: CMakeFiles/rnnoise.dir/rnnoise_wrapper.c.o
librnnoise.so: CMakeFiles/rnnoise.dir/build.make
librnnoise.so: CMakeFiles/rnnoise.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --bold --progress-dir=/Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/CMakeFiles --progress-num=$(CMAKE_PROGRESS_9) "Linking C shared library librnnoise.so"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/rnnoise.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/rnnoise.dir/build: librnnoise.so
.PHONY : CMakeFiles/rnnoise.dir/build

CMakeFiles/rnnoise.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/rnnoise.dir/cmake_clean.cmake
.PHONY : CMakeFiles/rnnoise.dir/clean

CMakeFiles/rnnoise.dir/depend:
	cd /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni /Users/lambiengcode/Documents/webrtc/flutter-webrtc/android/jni/CMakeFiles/rnnoise.dir/DependInfo.cmake "--color=$(COLOR)"
.PHONY : CMakeFiles/rnnoise.dir/depend

