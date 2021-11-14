//백엔드가 아닌 외부 요청시 사용되는 함수들 모음
import axios from "axios";
import qs from "qs";

function spotifyAccess() {
    const clientId = "c11ac86047554349be06a3cc291290d5";
    const clientSecret = "542117e848a84a488527f6b469f3cd3a";

    const headers = {
        headers: {
            Accept: "application/json",
            "Content-Type": "application/x-www-form-urlencoded",
        },
        auth: {
            username: clientId,
            password: clientSecret,
        },
    };
    const data = {
        grant_type: "client_credentials",
    };

    axios
        .post(
            "https://accounts.spotify.com/api/token",
            qs.stringify(data),
            headers
        )
        .then((res) => {
            localStorage.setItem("SPOTIFY_TOKEN", res.data.access_token);
        })
        .catch((err) => {
            console.log(err);
        });
}

async function searchMusic(input) {
    const headers = {
        headers: {
            Authorization: "Bearer " + localStorage.getItem('SPOTIFY_TOKEN'),
            Accept: "application/json",
            "Content-Type": "application/json",
        },
    };

    await axios
        .get(
            "https://api.spotify.com/v1/search?q=" + input + "&type=track",
            headers
        )
        .then((res) => {
            console.log(res.data.tracks.items);
            return res.data.tracks.items;
        })
        .catch((err) => {
            console.log(err);
        });
}

export default {
    spotifyAccess,
    searchMusic,
}