using System.Collections;
using System.Collections.Generic;
//using Cinemachine;
using UnityEngine;

public class CameraFollow : MonoBehaviour
{
    public Transform target;
    //public static CameraManager sharedInstance = null;
    //[HideInInspector]
    //public CinemachineVirtualCamera virtualCamera;

    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        // 카메라의 위치를 목표 트랜스폼의 위치에 일치
        transform.position = new Vector3(target.position.x, target.position.y+3, -10);
    }
}
