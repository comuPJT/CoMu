using System.Collections;
using System.Collections.Generic;
using Photon.Pun; // 유니티용 포톤 컴포넌트
using Photon.Realtime; // 포톤 실시간 네트워크 게임 개발용 라이브러리
using UnityEngine;
using UnityEngine.UI;

// 마스터(매치 메이킹) 서버와 룸 접속을 담당
public class LobbyManager : MonoBehaviourPunCallbacks // MonoBehaviour 확장 + 포톤 서비스 콜백 감지
{
    private string gameVersion = "1"; // 게임 버전

    public Text connectionInfoText; // 네트워크 정보를 표시할 텍스트
    public Button joinButton; // 룸 접속 버튼

    // 게임 실행과 동시에 마스터 서버 접속 시도
    private void Start()
    {
        // 접속에 필요한 정보(게임 버전) 설정
        PhotonNetwork.GameVersion = gameVersion;
        // 설정한 정보를 가지고 마스터 서버 접속 시도
        PhotonNetwork.ConnectUsingSettings();

        // 룸 접속 버튼을 잠시 비활성화
        joinButton.interactable = false;
        // 접속을 시도 중임을 텍스트로 표시
        connectionInfoText.text = "마스터 서버에 접속중...";
    }

    // 마스터 서버 접속 성공시 자동 실행
    public override void OnConnectedToMaster()
    {
        // 룸 접속 버튼을 활성화
        joinButton.interactable = true;
        // 접속 정보 표시
        connectionInfoText.text = "온라인 : 마스터 서버와 연결됨";
    }

    // 마스터 서버 접속 실패시 자동 실행
    // DisconnectCause 에 접속 끊김 이유 자동 입력됨
    public override void OnDisconnected(DisconnectCause cause)
    {
        // 룸 접속 버튼을 비활성화
        joinButton.interactable = false;
        // 접속 정보 표시
        connectionInfoText.text = "오프라인 : 마스터 서버와 연결되지 않음\n접속 재시도 중...";

        // 마스터 서버로의 재접속 시도
        PhotonNetwork.ConnectUsingSettings();
    }

    // 룸 접속 시도 (룸 접속 버튼 클릭 시 실행)
    public void Connect()
    {
        // 중복 접속 시도를 막기 위해, 접속 버튼 잠시 비활성화
        joinButton.interactable = false;

        // 마스터 서버에 접속중이라면
        if (PhotonNetwork.IsConnected)
        {
            // 룸 접속 실행
            connectionInfoText.text = "룸에 접속...";
            // 빈 무작위 룸에 접속 시도
            PhotonNetwork.JoinRandomRoom();
        }
        else
        {
            // 마스터 서버에 접속중이 아니라면, 마스터 서버에 접속 시도
            connectionInfoText.text = "오프라인 : 마스터 서버와 연결되지 않음\n접속 재시도 중...";
            // 마스터 서버로의 재접속 시도
            PhotonNetwork.ConnectUsingSettings();
        }
    }

    // (빈 방이 없어)랜덤 룸 참가에 실패한 경우 자동 실행
    public override void OnJoinRandomFailed(short returnCode, string message)
    {
        // 접속 상태 표시
        connectionInfoText.text = "빈 방이 없음, 새로운 방 생성...";
        // 최대 10명을 수용 가능한 빈방을 생성
        // 방 제목, 방 옵션을 인자로 받음
        // 생성된 룸은 리슨 서버 방식으로 동작: 룸을 생성한 클라이언트가 호스트 역할
        PhotonNetwork.CreateRoom(null, new RoomOptions { MaxPlayers = 10 });
    }

    // 룸에 참가 완료된 경우 자동 실행
    public override void OnJoinedRoom()
    {
        // 접속 상태 표시
        connectionInfoText.text = "방 참가 성공";
        // 모든 룸 참가자들이 Main 씬을 로드하게 함
        PhotonNetwork.LoadLevel("Main");
        /*
         * SceneManager.LoadScene("Main") 은 이전 씬의 오브젝트를 삭제하고 다음 씬을 로드하므로
         * 로비 씬의 네트워크 정보가 유지되지 않음.
         * 또, 플레이어들이 서로 동기화 없이 각자 Main 씬을 로드하기 때문에 다른 사람의 캐릭터가 보이지 않음
         * PhotonNetwork.LoadLevel("Main") 은 씬을 로드하고, 해당 씬의 구성이 플레이어 사이 동기화되도록 유지
         */
    }
}