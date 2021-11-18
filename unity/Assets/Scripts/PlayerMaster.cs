using UnityEngine;
using System.Runtime.InteropServices;
using Photon.Pun;

public class PlayerMaster : MonoBehaviourPunCallbacks
{
    // 플레이어의 캐릭터 번호 받아오기
    [DllImport("__Internal")]
    private static extern int GetMyCharacterNum();

    // 현재 방 이름 저장
    [DllImport("__Internal")]
    private static extern void SetRoomName(string name);

    public static string roomName;

    public float moveSpeed = 8.0f;
    Vector2 move = new Vector2();
    Vector3 prevPos;
    Vector2 moveVal = new Vector2();
    Rigidbody2D player;

    private SPUM_Prefabs _prefabs;
    public enum PlayerState
    {
        idle,
        move,
        attack,
        death,
    }
    public PlayerState _playerState = PlayerState.idle;

    void Start()
    {
        player = GetComponent<Rigidbody2D>();
        prevPos = this.transform.position;

        // 로컬 플레이어인 경우에만 적용
        if (photonView.IsMine)
        {
            SetCharacterNum(GetMyCharacterNum());
            //SetCharacterNum(5);
        }
        else
        {
            SetCharacterNum(10);
        }

        SetRoomName(PhotonNetwork.CurrentRoom.Name); // 메인 공간에서 시작
        roomName = PhotonNetwork.CurrentRoom.Name;
    }

    void Update()
    {

    }

    private void FixedUpdate()
    {
        // 리모트 플레이어 오브젝트의 위치 변화 감지
        moveVal.x = this.transform.position.x - prevPos.x;
        moveVal.y = this.transform.position.y - prevPos.y;
        prevPos = this.transform.position;

        // 로컬 플레이어인 경우에만 적용
        if (photonView.IsMine)
        {
            MoveCharacter();
        }
        UpdateState();
    }

    private void MoveCharacter()
    {
        move.x = Input.GetAxisRaw("Horizontal");
        move.y = Input.GetAxisRaw("Vertical");

        // 일정 속력 유지를 위한 정규화
        move.Normalize();

        // 이동 속도에 맞춰서 이동
        player.velocity = move * moveSpeed;
    }

    private void UpdateState()
    {
        
        // 이동하고 있을 때 애니메이션 활성화
        if (move.x > 0 || moveVal.x > 0)
        {
            // 오른쪽으로 가고 있을 때 캐릭터 방향 뒤집기
            _prefabs.transform.localScale = new Vector3(-1, 1, 1);
            _prefabs.PlayAnimation(1);
        }
        else if (move.x < 0 || moveVal.x < 0)
        {
            _prefabs.transform.localScale = new Vector3(1, 1, 1);
            _prefabs.PlayAnimation(1);
        }
        else if (move.y != 0 || moveVal.y != 0)
        {
            _prefabs.PlayAnimation(1);
        }
        else
        {
            _prefabs.PlayAnimation(0);
        }
    }

    private void OnTriggerEnter2D(Collider2D collision)
    {
        if (collision.gameObject.CompareTag("Main"))
        {
            // 메인 씬으로 돌아오기
            CameraFollow.isMain = true;

            // 메인으로 방 이름 변경
            SetRoomName("Main");

            roomName = "Main";
            PhotonNetwork.LeaveRoom();

        }
        else if (move.y > 0 && (collision.gameObject.CompareTag("MyRoom")
            || collision.gameObject.CompareTag("Theme1")
            || collision.gameObject.CompareTag("Theme2")
            || collision.gameObject.CompareTag("Theme3")
            || collision.gameObject.CompareTag("Theme4")
            || collision.gameObject.CompareTag("Theme5")))
        {
            // 현재 씬에 맞게 변수 값 설정
            CameraFollow.isMain = false;
            CameraFollow.isFirst = true;

            // 이동하는 곳으로 방 이름 변경
            SetRoomName(collision.gameObject.tag);

            // 문에 해당하는 씬으로 이동
            roomName = collision.gameObject.tag;
            PhotonNetwork.LeaveRoom();
        }
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
        _prefabs = this.transform.GetChild(num).GetComponent<SPUM_Prefabs>();
    }
}
