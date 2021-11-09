using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerPrefab : MonoBehaviour
{
    public static int characterNum = 2;

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