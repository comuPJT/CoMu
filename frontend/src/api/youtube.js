import axios from "axios";

async function getYouTubeUrl(artist, title) {
    const KEY = "AIzaSyBzeQSiAsD7ahiOBP4eSQ28hnZjVtrjGOA"
    var youtubesrc = "https://www.youtube.com/watch?v=";
    await axios
        .get(
            "https://www.googleapis.com/youtube/v3/search?key=" + KEY + "&part=id&q=" +
            artist +
            " " +
            title +
            " lyrics"
        )
        .then((res) => {
            youtubesrc += res.data.items[0].id.videoId;
        })
        .catch((err) => console.log(err.response));
    return youtubesrc
}

export default {
    getYouTubeUrl,
}