using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class PlayerMove : MonoBehaviour
{
    public float moveSpeed = 5.0f;
    Vector2 move = new Vector2();
    Rigidbody2D player;

    public SPUM_Prefabs _prefabs;
    public enum PlayerState
    {
        idle,
        move,
        attack,
        death,
    }
    public PlayerState _playerState = PlayerState.idle;

    // Start is called before the first frame update
    void Start()
    {
        player = GetComponent<Rigidbody2D>();
        _prefabs = transform.GetChild(PlayerPrefab.characterNum).GetComponent<SPUM_Prefabs>();
    }

    // Update is called once per frame
    void Update()
    {
        UpdateState();
    }

    private void FixedUpdate()
    {
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
        if (move.x > 0)
        {
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
            SceneManager.LoadScene("Main", LoadSceneMode.Single);
        }
        else if (move.y > 0 && (collision.gameObject.CompareTag("MyRoom")
            || collision.gameObject.CompareTag("Theme1")
            || collision.gameObject.CompareTag("Theme2")
            || collision.gameObject.CompareTag("Theme3")
            || collision.gameObject.CompareTag("Theme4")
            || collision.gameObject.CompareTag("Theme5")))
        {
            // 문에 해당하는 씬으로 이동
            SceneManager.LoadScene(collision.gameObject.tag, LoadSceneMode.Single);
        }
    }
}
