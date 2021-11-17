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
        if (!photonView.IsMine)
        {
            return;
        }
        // 받아온 유저 닉네임 설정
        // SetUserNickname(GetUserNickname());
        SetUserNickname("test");
    }

    void Update()
    {
        
    }

    private void SetUserNickname(string newNickname)
    {
        // TextMesh에 닉네임 설정
        nickname.text = newNickname;
    }
}
