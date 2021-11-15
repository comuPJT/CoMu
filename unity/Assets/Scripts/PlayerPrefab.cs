using UnityEngine;
using System.Runtime.InteropServices;

public class PlayerPrefab : MonoBehaviour
{
    // 플레이어의 캐릭터 번호 받아오기
    [DllImport("__Internal")]
    private static extern int GetMyCharacterNum();

    public static int characterNum = GetMyCharacterNum();

    // Start is called before the first frame update
    void Start()
    {
        // 모든 캐릭터 모델 상태 비활성화
        for (int i = 0; i < 11; i++)
        {
            this.transform.GetChild(i).gameObject.SetActive(false);
        }
        // 저장된 캐릭터 모델만 활성화
        this.transform.GetChild(characterNum).gameObject.SetActive(true);
    }

    // Update is called once per frame
    void Update()
    {

    }
}