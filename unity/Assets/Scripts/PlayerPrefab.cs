using UnityEngine;
using System.Runtime.InteropServices;

public class PlayerPrefab : MonoBehaviour
{
    // 플레이어의 캐릭터 번호 받아오기
    [DllImport("__Internal")]
    private static extern int GetMyCharacterNum();

    void Start()
    {
        // SetCharacterNum(GetMyCharacterNum());
        SetCharacterNum(5);
    }

    void Update()
    {

    }

    private void SetCharacterNum(int num)
    {
        // 모든 캐릭터 모델 상태 비활성화
        for (int i = 0; i < 11; i++)
        {
            this.transform.GetChild(i).gameObject.SetActive(false);
        }
        // 저장된 캐릭터 모델만 활성화
        this.transform.GetChild(num).gameObject.SetActive(true);
        PlayerMove._prefabs = transform.GetChild(num).GetComponent<SPUM_Prefabs>();
    }
}