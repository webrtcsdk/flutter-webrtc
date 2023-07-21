cmake_minimum_required(VERSION 3.10.2)

#rnnoise
add_library(rnnoise STATIC
        rnnoise/src/celt_lpc.c
        rnnoise/src/denoise.c
        rnnoise/src/kiss_fft.c
        rnnoise/src/pitch.c
        rnnoise/src/rnn_data.c
        rnnoise/src/rnn_reader.c
        rnnoise/src/rnn_reader.c
        rnnoise/src/rnn.c)
target_compile_options(rnnoise PUBLIC
        -Wall -finline-functions -fno-strict-aliasing -O3 -frtti -Wno-unknown-pragmas -funroll-loops -fexceptions -fno-math-errno)
set_target_properties(rnnoise PROPERTIES
        ANDROID_ARM_MODE arm)
target_compile_definitions(rnnoise PRIVATE
        HAVE_PTHREAD __STDC_LIMIT_MACROS BSD=1 USE_KISS_FFT NULL=0 SOCKLEN_T=socklen_t LOCALE_NOT_USED _LARGEFILE_SOURCE=1 _FILE_OFFSET_BITS=64 restrict= __EMX__ OPUS_BUILD USE_ALLOCA HAVE_LRINT HAVE_LRINTF
        _celt_autocorr=rnnoise__celt_autocorr
        celt_fir=rnnoise_celt_fir
        celt_iir=rnnoise_celt_iir
        _celt_lpc=rnnoise__celt_lpc
        celt_pitch_xcorr=rnnoise_celt_pitch_xcorr
        compute_band_corr=rnnoise_compute_band_corr
        compute_band_energy=rnnoise_compute_band_energy
        compute_dense=rnnoise_compute_dense
        compute_gru=rnnoise_compute_gru
        compute_rnn=rnnoise_compute_rnn
        interp_band_gain=rnnoise_interp_band_gain
        opus_fft_alloc=rnnoise_opus_fft_alloc
        opus_fft_alloc_arch_c=rnnoise_opus_fft_alloc_arch_c
        opus_fft_alloc_twiddles=rnnoise_opus_fft_alloc_twiddles
        opus_fft_c=rnnoise_opus_fft_c
        opus_fft_free=rnnoise_opus_fft_free
        opus_fft_free_arch_c=rnnoise_opus_fft_free_arch_c
        opus_fft_impl=rnnoise_opus_fft_impl
        opus_ifft_c=rnnoise_opus_ifft_c
        pitch_downsample=rnnoise_pitch_downsample
        pitch_filter=rnnoise_pitch_filter
        pitch_search=rnnoise_pitch_search
        remove_doubling=rnnoise_remove_doubling)
target_compile_definitions(rnnoise PUBLIC
        WEBRTC_HAVE_DCSCTP RTC_DISABLE_TRACE_EVENTS WEBRTC_OPUS_SUPPORT_120MS_PTIME=1 BWE_TEST_LOGGING_COMPILE_TIME_ENABLE=0 ABSL_ALLOCATOR_NOTHROW=1 RTC_ENABLE_VP9 WEBRTC_POSIX WEBRTC_LINUX WEBRTC_ANDROID WEBRTC_USE_H264 NDEBUG WEBRTC_HAVE_USRSCTP WEBRTC_HAVE_SCTP WEBRTC_APM_DEBUG_DUMP=0 WEBRTC_USE_BUILTIN_ISAC_FLOAT WEBRTC_OPUS_VARIABLE_COMPLEXITY=0 HAVE_NETINET_IN_H WEBRTC_INCLUDE_INTERNAL_AUDIO_DEVICE __Userspace__ SCTP_SIMPLE_ALLOCATOR SCTP_PROCESS_LEVEL_LOCKS __Userspace_os_Linux)
target_include_directories(rnnoise PUBLIC
        rnnoise/include)