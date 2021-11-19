using UnityEngine;
using TMPro;
using System.Runtime.InteropServices;
using Photon.Pun;

public class NicknameScript : MonoBehaviourPun
{
    // 플레이어의 닉네임 받아오기
    [DllImport("__Internal")]
    private static extern string GetUserNickname();

    public TextMeshPro nickname;

    void Start()
    {
        // 로컬 플레이어인 경우에만 적용
        if (photonView.IsMine)
        {
            // 받아온 유저 닉네임 설정
            string userNickname = GetUserNickname();
            //string userNickname = "test";
            SetUserNickname(userNickname);
            photonView.Owner.NickName = userNickname;
        }
        else
        {
            SetUserNickname(photonView.Owner.NickName);
        }
    }

    void Update()
    {
        
    }

    private void SetUserNickname(string newNickname)
    {
        // TextMesh에 닉네임 설정
        nickname.text = newNickname;
        // 로컬 플레이어인 경우에만 닉네임 컬러 다르게 설정
        if (photonView.IsMine)
        {
            nickname.color = new Color(0.5f, 1, 0);
        }
    }
}
