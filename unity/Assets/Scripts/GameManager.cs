using Photon.Pun;
using Photon.Realtime;
using UnityEngine;
using UnityEngine.SceneManagement;
using System.Runtime.InteropServices;

// 점수와 게임 오버 여부, 게임 UI를 관리하는 게임 매니저
public class GameManager : MonoBehaviourPunCallbacks, IPunObservable
{
    // 외부에서 싱글톤 오브젝트를 가져올때 사용할 프로퍼티
    /*public static GameManager instance
    {
        get
        {
            // 만약 싱글톤 변수에 아직 오브젝트가 할당되지 않았다면
            if (m_instance == null)
            {
                // 씬에서 GameManager 오브젝트를 찾아 할당
                m_instance = FindObjectOfType<GameManager>();
            }

            // 싱글톤 오브젝트를 반환
            return m_instance;
        }
    }

    private static GameManager m_instance;*/ // 싱글톤이 할당될 static 변수

    public GameObject playerPrefab; // 생성할 플레이어 캐릭터 프리팹

    // 주기적으로 자동 실행되는, 동기화 메서드
    public void OnPhotonSerializeView(PhotonStream stream, PhotonMessageInfo info)
    {

    }

    /*private void Awake()
    {
        // 씬에 싱글톤 오브젝트가 된 다른 GameManager 오브젝트가 있다면
        if (instance != this)
        {
            // 자신을 파괴
            PhotonNetwork.Destroy(gameObject);
            Destroy(gameObject);
        }
    }*/

    private Vector3[] posList = 
        { new Vector3(0, 0, 0), new Vector3(-5.5f, 17.5f, 0), 
        new Vector3(1.35f, 17.5f, 0), new Vector3(8.3f, 17.5f, 0), 
        new Vector3(15.3f, 17.5f, 0), new Vector3(22.35f, 17.5f, 0), 
        new Vector3(29.37f, 3f, 0) };

    // 게임 시작과 동시에 플레이어가 될 게임 오브젝트를 생성
    private void Start()
    {
        int num = 0;
        string pRoomName = PrevRoomName.prevRoomName;
        if (pRoomName == "Main")
        {
            num = 0;
        }
        else
        {
            if (pRoomName == "Theme1") num = 1;
            else if (pRoomName == "Theme2") num = 2;
            else if (pRoomName == "Theme3") num = 3;
            else if (pRoomName == "Theme4") num = 4;
            else if (pRoomName == "Theme5") num = 5;
            else if (pRoomName == "MyRoom") num = 6;
        }
        // 네트워크 상의 모든 클라이언트들에서 생성 실행
        // 단, 해당 게임 오브젝트의 주도권은, 생성 메서드를 직접 실행한 클라이언트에게 있음
        PhotonNetwork.Instantiate("PlayerObject", posList[num], Quaternion.identity);
    }

    public override void OnConnectedToMaster()
    {
        // 다른 룸으로
        PhotonNetwork.JoinOrCreateRoom(PlayerMaster.roomName, new RoomOptions { MaxPlayers = 20 }, TypedLobby.Default);
    }

    public override void OnJoinedRoom()
    {
        PhotonNetwork.LoadLevel(PhotonNetwork.CurrentRoom.Name);
    }

    public override void OnJoinRoomFailed(short returnCode, string message)
    {
        Debug.LogFormat("RoomJoinFailed, RoomName is {0}", PlayerMaster.roomName);
        PhotonNetwork.JoinOrCreateRoom(PlayerMaster.roomName, new RoomOptions { MaxPlayers = 20 }, TypedLobby.Default);
    }
}
