using UnityEngine;
using UnityEngine.SceneManagement;
using System.Runtime.InteropServices;
using Photon.Pun;
using Photon.Realtime;

public class PlayerMove : MonoBehaviourPunCallbacks
{
    // 현재 방 이름 저장
    [DllImport("__Internal")]
    private static extern void SetRoomName(string name);

    public static string roomName;

    public float moveSpeed = 8.0f;
    Vector2 move = new Vector2();
    Rigidbody2D player;

    public static SPUM_Prefabs _prefabs;
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
        SetRoomName("Main"); // 메인 공간에서 시작
        roomName = "Main";
    }

    void Update()
    {

    }

    private void FixedUpdate()
    {
        // 로컬 플레이어인 경우에만 적용
        if (!photonView.IsMine)
        {
            return;
        }
        UpdateState();
        MoveCharacter();
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
        if (move.x > 0)
        {
            // 오른쪽으로 가고 있을 때 캐릭터 방향 뒤집기
            _prefabs.transform.localScale = new Vector3(-1, 1, 1);
            _prefabs.PlayAnimation(1);
        }
        else if (move.x < 0)
        {
            _prefabs.transform.localScale = new Vector3(1, 1, 1);
            _prefabs.PlayAnimation(1);
        }
        else if (move.y != 0)
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
}
