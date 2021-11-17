using UnityEngine;
using TMPro;
using System.Runtime.InteropServices;

public class NicknameScript : MonoBehaviour
{
    // 플레이어의 닉네임 받아오기
    [DllImport("__Internal")]
    private static extern string GetUserNickname();

    public TextMeshPro nickname;

    void Start()
    {
        // 받아온 유저 닉네임 설정
        SetUserNickname(GetUserNickname());
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
