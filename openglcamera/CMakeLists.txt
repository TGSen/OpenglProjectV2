# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)
file(GLOB s_source src/main/cpp/*.cpp src/main/cpp/*.c)
# Gradle automatically packages shared libraries with your APK.
include_directories(src/main/jniLibs/include)
set(LibDir ${CMAKE_CURRENT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI})
add_library( # Sets the name of the library.
             sgles
             # Sets the library as a shared library.
             SHARED
             # Provides a relative path to your source file(s).
             ${s_source}
             )
#GitHub开源定位五官点
add_subdirectory(src/main/jniLibs/FaceAlignment)
include_directories(src/main/jniLibs/FaceAlignment/include)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )
 find_library( # Sets the name of the path variable.
               jnigraphics-lib


               # Specifies the name of the NDK library that
               # you want CMake to locate.
               jnigraphics)

  add_library(
               libopencv_java3
               SHARED
               IMPORTED)
   set_target_properties(
               libopencv_java3
               PROPERTIES IMPORTED_LOCATION
                ${LibDir}/libopencv_java3.so)

#设置编译指令

#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
#判断编译器类型,如果是gcc编译器,则在编译选项中加入c++11支持
if(CMAKE_COMPILER_IS_GNUCXX)
    set(CMAKE_CXX_FLAGS "-std=c++11 ${CMAKE_CXX_FLAGS}")
    message(STATUS "optional:-std=c++11")
endif(CMAKE_COMPILER_IS_GNUCXX)

#如果当前系统版本过低设置拒绝编译
if (${ANDROID_PLATFORM_LEVEL} LESS 11)
  message(FATAL_ERROR "OpenGL 2 is not supported before API level 11 (currently using ${ANDROID_PLATFORM_LEVEL}).")
  return()
elseif (${ANDROID_PLATFORM_LEVEL} LESS 18)
  add_definitions("-DDYNAMIC_ES3")
  set(OPENGL_LIB GLESv2)
else ()
#set(OPENGL_LIB GLESv3)
set(OPENGL_LIB GLESv3)

endif (${ANDROID_PLATFORM_LEVEL} LESS 11)


# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       sgles
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       ${jnigraphics-lib}
                       libopencv_java3
                       ${OPENGL_LIB}
                        android
                        seeta_fa_lib
                        EGL)