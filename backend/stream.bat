@echo off

set roomid=%1
set music=%2

IF NOT EXIST %music%.mp4  bash -c "wget https://comu304.s3.ap-northeast-2.amazonaws.com/static/%music%.mp4"

bash -c "ffmpeg -re -i %music%.mp4 -vcodec copy -loop -1 -c:a aac -b:a 160k -ar 44100 -strict -2 -f flv rtmp://k5a304.p.ssafy.io/live%roomid%/%music%"
del %music%.mp4
