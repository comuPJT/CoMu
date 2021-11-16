#!/bin/bash

roomid=$1
music=$2

if [ ! -e $music.mp4 ];then
	wget https://comu304.s3.ap-northeast-2.amazonaws.com/static/$music.mp4
fi

if [ $? -eq 0 ]
then 
	ffmpeg -re -i $music.mp4 -vcodec copy -loop -1 -c:a aac -b:a 160k -ar 44100 -strict -2 -f flv rtmp://k5a304.p.ssafy.io/live$roomid/$music
	rm $music.mp4
	exit 0
else 
	echo "AWS s3로 부터 $music.mp4를 받아오지 못했습니다."
	exit 1
fi

