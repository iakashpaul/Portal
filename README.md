# Portal
Android app for the ```Hole in the Palm``` project, making LLMs accessible on-device!

## Roadmap

1. Build basic Android app with ```llama.cpp``` as a library for Android NDK with ```OpenBLAS``` and ```CLBlast``` support if feasible, since I've already verified 3b & 7b models running at decent tok/s at Q4/5 based quantization levels using Termux & Userland

2. Verify models for general QnA, code assistance & multi-lingual support

3. Add support to incorporate datasets for RAG based QnA on-device & toggling to code assistant roles

### Long term goal
[ ] Look at incorporating STT via on-device STT or Whisper model for translation into English from regional language directly & TTS with VAD based interruptibility

[ ] Android WearOS app for handsfree interactions
