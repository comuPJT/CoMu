using UnityEngine;
using Photon.Pun; // 포톤 유니티 라이브러리

public class CameraFollow : MonoBehaviourPun
{
    public Transform target;

    public static bool isMain; // 현재 화면이 메인인지 여부
    public static bool isFirst; // 방에 들어오고 처음인지 여부

    // Start is called before the first frame update
    void Start()
    {
        isMain = true;
        isFirst = true;
    }

    // Update is called once per frame
    void Update()
    {
        // 로컬 플레이어인 경우에만 카메라 따라가기
        if (!photonView.IsMine) // 현재 오브젝트가 로컬 오브젝트인 경우에만 true 값
        {
            return;
        }
        if (isMain)
        {
            // 카메라의 위치를 목표 트랜스폼의 위치에 일치
            float x = target.position.x;
            float y = target.position.y + 4;

            // 카메라의 위치 제한 범위 내로 한정
            if (x < 3)
            {
                x = 3;
            }
            else if (x > 21)
            {
                x = 21;
            }
            if (y < 6)
            {
                y = 6;
            }
            else if (y > 15)
            {
                y = 15;
            }
            // 목표하는 위치로 카메라의 위치 조정
            transform.position = new Vector3(x, y, -10);
        }
        else if (isFirst)
        {
            // 카메라의 위치를 고정
            transform.position = new Vector3(target.position.x, target.position.y + 6, -10);
            isFirst = false;
        }
    }
}
