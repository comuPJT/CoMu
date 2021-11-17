using Cinemachine; // 시네머신 관련 코드
using Photon.Pun; // PUN 관련 코드
using UnityEngine;

// 시네머신 카메라가 로컬 플레이어를 추적하도록 설정
public class CameraSetup : MonoBehaviourPun
{
    /*public static bool isMain; // 현재 화면이 메인인지 여부
    public static bool isFirst; // 방에 들어오고 처음인지 여부*/

    void Start()
    {
        /*isMain = true;
        isFirst = true;*/

        if (photonView.IsMine)
        {
            // 씬에 있는 시네머신 가상 카메라를 찾고
            CinemachineVirtualCamera followCam = FindObjectOfType<CinemachineVirtualCamera>();
            // 가상 카메라의 추적 대상을 자신의 트랜스폼으로 변경
            followCam.Follow = transform;
            // followCam.LookAt = transform;
        }

    }

    /*void Awake()
    {
        DontDestroyOnLoad(gameObject);
    }*/

    /*void Update()
    {
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
    }*/
}