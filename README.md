# Portal
Android app for the ```Hole in the Palm``` project, making LLMs accessible on-device!

## Roadmap

1. Build an Android app with ```llama.cpp/server.cpp``` as a library via Android NDK (with ```OpenBLAS``` and ```CLBlast``` support if feasible), since I've already verified 3b & 7b models running at decent tok/s at Q4/5 based quantization levels using Termux & Userland

2. Verify models for general chat, code assistance & multi-lingual support-
   
   [ ] microsoft/phi-2 
   
   [ ] stabilityai/stable-code-3b

   [ ] mistralai/Mistral-7B-Instruct-v0.2

   [ ] Tensoic/Kan-LLaMa-SFT-v0.1

   [ ] sarvamai/OpenHathi-7B-Hi-v0.1-Base   

4. Add support to incorporate custom data & vector-db for RAG based QnA on-device & toggling to code assistance tasks

### Long term goal

[ ] Look at incorporating on-device STT or Whisper based STT model for translation into English from regional language directly & speak responses via TTS with VAD based interruptibility

[ ] Android WearOS app for handsfree interactions
