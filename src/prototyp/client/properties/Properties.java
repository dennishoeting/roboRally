package prototyp.client.properties;

import com.google.gwt.i18n.client.Messages;

/**
 * Klasse zur Nutzung der Properties.properties. Funktionsweise: Jedem Werte-Tupel ist eine Klasse zugeordnet, die den namentlich
 * dem jeweiligen Tupel entspricht. In der Anwendung wird ein Properties-Objekt mit GWT.create() erzeugt, die Methoden liefern den
 * entsprechenden Wert.
 * 
 * @author Dennis, Jens
 * @version 2.0
 */
public interface Properties extends Messages {
	String administrationPage_buttonBanAccount_title_lock();

	String administrationPage_buttonBanAccount_title_unlock();

	String administrationPage_editUser_text_success();
	
	String administrationPage_editUser_text_success_logout();

	String administrationPage_editUser_title();

	String administrationPage_information_text();

	String administrationPage_lockedGridField_title();

	String administrationPage_lockUser_question();

	String administrationPage_lockUser_text_success();

	String administrationPage_lockUser_title();

	String administrationPage_rightArea_groupTitle();

	String administrationPage_searchUserTextItem_title();

	String administrationPage_searchUserTextItem_tooltip();

	String administrationPage_sqlException();

	String administrationPage_title();

	String administrationPage_unlockUser_question();

	String administrationPage_unlockUser_text_success();

	String administrationPage_unlockUser_title();

	String administrationPage_userIDGridField_title();

	String administrationPage_userListArea_groupTitle();

	String backupwindow_question();

	String backupwindow_title();
	
	String slowConnectionWindowTitle();
	
	String chatPresenterCountDownStart();

	String chatPresenterEnterGame();

	String chatPresenterLeaveGame();

	String chatPresenterLeavePostGame();

	String chatPresenterWhisper();

	String colorBLUE();

	String colorBROWN();

	String colorGREEN();

	String colorGREY();

	String colorLIGHT_BLUE();

	String colorLIGHT_GREEN();

	String colorORANGE();

	String colorPINK();

	String colorRED();

	String colorTURQUOISE();

	String colorVIOLET();

	String colorYELLOW();

	String enemiesProfile_title_awardsArea();
	
	String roundErrorSlowConnection();

	String eventRoundRemovePlayerWatcherEvent();

	String eventRoundRoundCancelledEvent();

	String exception_field_conveyorBeltException();

	String exception_frontpage_alreadyInGameException();

	String exception_frontpage_cantLoadAgbsException();

	String exception_frontpage_failedToSendMailException();

	String exception_frontpage_invalidLogInDataException();

	String exception_frontpage_sameBrowserException();

	String exception_frontpage_tooManyGeneratedPassWordsException();

	String exception_frontpage_userNotInDataBaseException();

	String exception_lobby_photoUploadException();

	String exception_lobby_unable2LoadUserException();

	String exception_lobby_usersBrowserCrashedException();

	String exception_mail_accountLockedException();

	String exception_mail_eMailAlreadyExistsException();

	String exception_mail_mailNotFoundException();

	String exception_map_cantSavePlayingBoardException();

	String exception_map_playingBoardIsLockedException();

	String exception_mapgenerator_cantLoadPlayingBoardException();

	String exception_nicknameAlreadyExitsException();

	String exception_noMapFoundException();

	String exception_noModificationException();

	String exception_passwordGenerationException();

	String exception_playingboard_chechpointfieldNumeratedException();

	String exception_playingboard_checkpointfieldnotreachableException_part1();

	String exception_playingboard_checkpointfieldnotreachableException_part2();

	String exception_playingboard_checkpointfieldnotreachableException_part3();

	String exception_playingboard_checkpointfieldnotreachableException_part4();

	String exception_playingboard_compactorFieldException_part1();

	String exception_playingboard_compactorFieldException_part2();

	String exception_playingboard_compactorFieldException_part3();

	String exception_playingboard_lessThanTwoStartFieldsException();

	String exception_playingboard_noCheckointFieldException();

	String exception_playingboard_pusherfieldException_part1();

	String exception_playingboard_pusherfieldException_part2();

	String exception_playingboard_repairfieldnotreachableException_part1();

	String exception_playingboard_repairfieldnotreachableException_part2();

	String exception_playingboard_startfieldNumeratedException();

	String exception_pregame_colorAlreadyExistsException();

	String exception_roundNotThereException();

	String exception_tooManyFailedLoginsException();

	String exception_userAlreadyOnlineException();

	String exception_userLockedException();

	String exception_wrongPassword();

	String frontPage_buttonSendPassword_text();

	String frontPage_eMailField_title();

	String frontPage_login_text();

	String frontPage_passwortLost_buttonSend_title();

	String frontPage_sendPassword_success_title();

	String frontPage_title();

	String frontPage_window_exception_title();

	String global_buttonWidth();

	String global_marginBetweenStackAreas();

	String global_marginInStackAreas();

	String global_paddingInStackAreas();

	String global_path_localhostImagesAwards();

	String global_path_localhostImagesFields();

	String global_path_localhostImagesIcons();

	String global_path_localhostImagesMapGenerator();

	String global_path_localhostImagesMaps();

	String global_path_localhostImagesRobots();

	String global_path_localhostImagesTemp();

	String global_path_localhostImagesUi();

	String global_selectedPlayingBoard();

	String global_symbol_hash();

	String global_title_abort();

	String global_title_attention();

	String global_title_awards();

	String global_title_badEMailMessage();

	String global_title_buttonReady();

	String global_title_chat();

	String global_title_close();

	String global_title_continue();

	String global_title_description();

	String global_title_editProfile();

	String global_title_email();

	String global_title_enemiesProfile();

	String global_title_error();

	String global_title_firstname();

	String global_title_game();

	String global_title_id();

	String global_title_logIn();

	String global_title_name();

	String global_title_nickname();

	String global_title_no();

	String global_title_off();

	String global_title_on();

	String global_title_optionalFields();

	String global_title_password();

	String global_title_passwordFields();

	String global_title_preference();

	String global_title_preview();

	String global_title_register();

	String global_title_requiredFields();

	String global_title_requiredMessage();

	String global_title_reset();

	String global_title_seconds();

	String global_title_send();

	String global_title_statistics();

	String global_title_submit();

	String global_title_surname();

	String global_title_type();

	String global_title_yes();

	String highScorePage_enemiesProfile_title();

	String highScorePage_highScoreGrid_aborted();

	String highScorePage_highScoreGrid_losts();

	String highScorePage_highScoreGrid_playedGames();

	String highScorePage_highScoreGrid_points();

	String highScorePage_highScoreGrid_rank();

	String highScorePage_highScoreGrid_wins();

	String highScorePage_title();

	String highScorePage_title_rankList();

	String highScorePage_userSearchItem_text();

	String informationWindow_cards_text();

	String informationWindow_cards_title();

	String informationWindow_dead_me_text();

	String informationWindow_dead_other_text_part1();

	String informationWindow_dead_other_text_part2();

	String informationWindow_dead_title();

	String lobbyPage_availableRounds_title();

	String lobbyPage_buttonCreateRound_text();

	String lobbyPage_buttonJoinRound_text();

	String lobbyPage_buttonWatchRound_text();

	String lobbyPage_gameListGrid_emptyMessage();

	String lobbyPage_getRoundException();

	String lobbyPage_getRoundsException();

	String lobbyPage_joinRound_noEntry();

	String lobbyPage_joinRound_title();

	String lobbyPage_mapType();

	String lobbyPage_menueGrid_difficulty_text();

	String lobbyPage_menueGrid_mapName_text();

	String lobbyPage_menueGrid_playerCount_text();

	String lobbyPage_menueGrid_timeElapsed_text();

	String lobbyPage_menueGrid_watcherCount_text();

	String lobbyPage_preview_title();

	String lobbyPage_previewListGrid_emptyMessage();

	String lobbyPage_title();

	String logOutPage_confirm_title();

	String logOutPage_confirm2_title_part1();

	String logOutPage_confirm2_title_part2();

	String logOutPage_confirm2_title_part3();

	String logOutPage_confirm3_title();

	String logOutPage_title();

	String mapGenerator_transformWindow_title();

	String mapGenerator_transformWindow_buttonChangeSize_tooltip();
	
	String mapGenerator_transformWindow_buttonFlipX_tooltip();
	
	String mapGenerator_transformWindow_buttonFlipY_tooltip();
	
	String mapGenerator_transformWindow_buttonRotateRight_tooltip();
	
	String mapGenerator_transformWindow_buttonRotateLeft_tooltip();
	
	String mapGenerator_transformWindow_buttonRotateFully_tooltip();
	
	String mapGeneratorChangeSizeWindow_title();

	String mapGeneratorDeletePopUp_abortButton();

	String mapGeneratorDeletePopUp_deleteButton();

	String mapGeneratorDeletePopUp_deleteQuestionText();

	String mapGeneratorEditingWindow_BasicField();

	String mapGeneratorEditingWindow_buildPusherDirectionArea_active();

	String mapGeneratorEditingWindow_buildPusherDirectionArea_buttonStack();

	String mapGeneratorEditingWindow_CheckpointField();

	String mapGeneratorEditingWindow_CheckpointNumber();

	String mapGeneratorEditingWindow_checkpointSection();

	String mapGeneratorEditingWindow_checkpointSection_ButtonStack();

	String mapGeneratorEditingWindow_CompactorField();

	String mapGeneratorEditingWindow_compactorSection_active();

	String mapGeneratorEditingWindow_compactorSection_ButtonStack();

	String mapGeneratorEditingWindow_ConveyorBeltField();

	String mapGeneratorEditingWindow_ConveyorBeltFieldRange();

	String mapGeneratorEditingWindow_createButton();

	String mapGeneratorEditingWindow_deleteButton();

	String mapGeneratorEditingWindow_direction();

	String mapGeneratorEditingWindow_east();

	String mapGeneratorEditingWindow_east_short();

	String mapGeneratorEditingWindow_Feldspezifiktion();

	String mapGeneratorEditingWindow_fieldTypeBox();

	String mapGeneratorEditingWindow_GearField();

	String mapGeneratorEditingWindow_gearSection();

	String mapGeneratorEditingWindow_HoleField();

	String mapGeneratorEditingWindow_InDirection();

	String mapGeneratorEditingWindow_laserSection();

	String mapGeneratorEditingWindow_left();

	String mapGeneratorEditingWindow_loadButton();

	String mapGeneratorEditingWindow_menueOff();

	String mapGeneratorEditingWindow_menueOn();

	String mapGeneratorEditingWindow_north();

	String mapGeneratorEditingWindow_north_short();

	String mapGeneratorEditingWindow_optimalWays();

	String mapGeneratorEditingWindow_OutDirection();

	String mapGeneratorEditingWindow_PusherField();

	String mapGeneratorEditingWindow_pusherSection();

	String mapGeneratorEditingWindow_RepairField();

	String mapGeneratorEditingWindow_repairSection();

	String mapGeneratorEditingWindow_repairSection_buttonStack();

	String mapGeneratorEditingWindow_right();

	String mapGeneratorEditingWindow_RotateConveyorBeltSection();

	String mapGeneratorEditingWindow_saveButton();

	String mapGeneratorEditingWindow_south();

	String mapGeneratorEditingWindow_south_short();

	String mapGeneratorEditingWindow_StartDirection();

	String mapGeneratorEditingWindow_StartField();

	String mapGeneratorEditingWindow_StartNumber();

	String mapGeneratorEditingWindow_title();

	String mapGeneratorEditingWindow_transformButton();

	String mapGeneratorEditingWindow_unloadButton();

	String mapGeneratorEditingWindow_Waende();

	String mapGeneratorEditingWindow_west();

	String mapGeneratorEditingWindow_west_short();

	String mapGeneratorLoadingPopUp_abortButton();

	String mapGeneratorLoadingPopUp_listGridItem_ID();

	String mapGeneratorLoadingPopUp_listGridItem_imageFileName();

	String mapGeneratorLoadingPopUp_listGridItem_maxPlayers();

	String mapGeneratorLoadingPopUp_listGridItem_name();

	String mapGeneratorLoadingPopUp_listGridItem_nickname();

	String mapGeneratorLoadingPopUp_listGridItem_numberOfCheckpoints();

	String mapGeneratorLoadingPopUp_listGridItem_size();

	String mapGeneratorLoadingPopUp_okButton();

	String mapGeneratorLoadingPopUp_searchItem_title();

	String mapGeneratorLoadingPopUp_selection_error();

	String mapGeneratorLoadingPopUp_title();

	String mapGeneratorMinimapWindow_title();

	String mapGeneratorPage_button_saveMap();

	String mapGeneratorPage_progressWindow_changeSize();

	String mapGeneratorPage_progressWindow_create();

	String mapGeneratorPage_progressWindow_flipX();

	String mapGeneratorPage_progressWindow_flipY();

	String mapGeneratorPage_progressWindow_load();

	String mapGeneratorPage_progressWindow_rotate();

	String mapGeneratorPage_progressWindow_rotateLeft();

	String mapGeneratorPage_progressWindow_rotateRight();

	String mapGeneratorPage_progressWindow_upload();

	String mapGeneratorPage_title();

	String mapGeneratorPopUp_abortButton();

	String mapGeneratorPopUp_aspectRatioException();

	String mapGeneratorPopUp_button_title_height();

	String mapGeneratorPopUp_button_title_width();

	String mapGeneratorPopUp_mapSizeException_large();

	String mapGeneratorPopUp_mapSizeException_small();

	String mapGeneratorPopUp_numberFormatException();

	String mapGeneratorPopUp_okButton();

	String mapGeneratorPopUp_title();

	String mapGeneratorPresenter_createMap_error_message();

	String mapGeneratorPresenter_loadMap_error_message();

	String mapGeneratorPresenter_progressWindow_progress();

	String mapGeneratorPresenter_unloadMap_error_message();

	String mapGeneratorSaveWindow_difficulty();

	String mapGeneratorSaveWindow_leicht_title();

	String mapGeneratorSaveWindow_normal_title();

	String mapGeneratorSaveWindow_schwer_title();

	String mapGeneratorSaveWindow_sehrLeicht_title();

	String mapGeneratorSaveWindow_sehrSchwer_title();

	String mapGeneratorSaveWindow_success();

	String page_button_wait();

	String page_textItem_failure_nonumbers();

	String page_textItem_failure_nospecialchars();

	String playerRecord_isPlayer();

	String playerRecord_notReady();

	String playerRecord_observer();

	String playerRecord_ready();

	String playerRecord_readyWatcher();

	String playerStatusArea_deadInfoSrc();

	String playerStatusArea_powerDownInfoSrc();

	String postGamePage_awardDescriptionField();

	String postGamePage_awardNameField();

	String postGamePage_chickn();

	String postGamePage_chicknDesc();

	String postGamePage_dark();

	String postGamePage_darkDesc();

	String postGamePage_deathsGridField();

	String postGamePage_dizzy();

	String postGamePage_dizzyDesc();

	String postGamePage_hamster();

	String postGamePage_hamsterDesc();

	String postGamePage_help();

	String postGamePage_helpDesc();

	String postGamePage_hitsListGrid();

	String postGamePage_karate();

	String postGamePage_karateDesc();

	String postGamePage_megaT();

	String postGamePage_megaTDesc();

	String postGamePage_noStatistic_text();

	String postGamePage_powerDownsGridField();

	String postGamePage_press();

	String postGamePage_pressDesc();

	String postGamePage_samariter();

	String postGamePage_samariterDesc();

	String postGamePage_snail();

	String postGamePage_snailDesc();

	String postGamePage_steps();

	String postGamePage_stepsDesc();

	String postGamePage_title();

	String postGamePage_victim();

	String postGamePage_victimDesc();

	String postGamePage_winner();

	String postGamePage_winnerDesc();

	String postGamePage_winnerNameField();
	
	String postGamePage_rotationsGridField();
	
	String postGamePage_hitOthersGridField();
	
	String postGamePage_stepsGridField();
	
	String postGamePage_numberOfReachedCheckpointsGridField();
	
	String postGamePage_repairsGridField();
	
	String postGamePage_robotsPushedInHoleGridField();

	String preGameGameInitiatorPage_buttonRemovePlayerWatcher_title();

	String preGameGameInitiatorPage_buttonStart_title();

	String preGameGameInitiatorPage_start_morePlayerPossible();

	String preGameGameInitiatorPage_start_noPlayerToStart();

	String preGameGameInitiatorPage_title();

	String preGameGameInitiatorPagePresenter_cancelRoundAsk();

	String preGamePage_buttonReady_title();

	String preGamePage_chat();

	String preGamePage_colorChooserTitle();

	String preGamePage_colorListGridField();

	String preGamePage_gameName();

	String preGamePage_nameListGridField();

	String preGamePage_observerListGridField();

	String preGamePage_playerListArea();

	String preGamePage_playerOptionsArea();

	String preGamePage_previewArea();

	String preGamePage_readyListGridField();

	String preGamePage_title();

	String preGamePage_userIDListGridField();

	String preGamePagePresenter_notReady();

	String preGamePagePresenter_randomColor();

	String preGamePagePresenter_ready();

	String preview_image_tooltip();

	String preview_playingboard_desciption_title();

	String preview_playingboard_difficulty_title();

	String preview_playingboard_maptype_title();

	String preview_playingboard_maxPlayers_title();

	String preview_playingboard_numberOfCheckPoints_title();

	String preview_playingboard_size_title();

	String preview_roundoption_allowObservers_title();

	String preview_roundoption_alwaysCBOn_title();

	String preview_roundoption_alwaysGearOn_title();

	String preview_roundoption_alwaysPresserOn_title();

	String preview_roundoption_alwaysPusherOn_title();

	String preview_roundoption_countDownTime_title();

	String preview_roundoption_laserOn_title();

	String preview_roundoption_playerSlots_title();

	String preview_roundoption_robotShootsOn_title();

	String preview_roundoption_timeCountdownOn_title();

	String preview_roundoption_watcherSlots_title();

	String previewArea_roundPreferencesRecord_other();

	String previewArea_roundPreferencesRecord_playingBoard();

	String previewArea_roundPreferencesRecord_roundOptions();

	String profilePage_accountDataArea_groupTitle();

	String profilePage_awardsArea_groupTitle();

	String profilePage_buttonEditPassword_title();

	String profilePage_editPassword_text_success();

	String profilePage_editPassword_title();

	String profilePage_editPassword_wrongPassword_failure();

	String profilePage_lastGamesArea_groupTitle();

	String profilePage_passwordNew_title();

	String profilePage_passwordNew2_title();

	String profilePage_passwordOld_title();

	String profilePage_photoUploadArea();

	String profilePage_photoUploadError();

	String profilePage_statisticDataArea_groupTitle();

	String profilePage_statsArea_groupTitle();

	String profilePage_title();

	String profilePagePresenter_noAwards_text();

	String profilePagePresenter_showAwards_text();

	String profilePictureWindow_title();

	String progressBar_mainLabel();

	String prototyp_childTabManager_height();

	String prototyp_childTabManager_width();

	String prototyp_mainTab_text();

	String refereePage_area_listArea_groupTitle();

	String refereePage_area_optionsArea_groupTitle();

	String refereePage_area_rightArea_groupTitle();

	String refereePage_countDownTime_title();

	String refereePage_playerSlots_title();

	String refereePage_table_maxPlayersListGridField_title();

	String refereePage_table_nameListGridField_title();

	String refereePage_table_numberOfCheckpointsListGridField_title();

	String refereePage_table_sizeListGridField_title();

	String refereePage_title();

	String refereePage_watcherSlots_title();

	String registrationPage_agb_title();

	String registrationPage_agbCheckBox_title();

	String registrationPage_agbs_text();

	String registrationPage_passNotMatch();

	String registrationPage_password2_title();

	String registrationPage_title();

	String registrationPage_welcome();

	String registrationPage_welcome_titel();

	String roundOptionPage_chatOptions_title();

	String roundOptionPage_closeButton_title();

	String roundOptionPage_divOptions_title();

	String roundOptionPage_gameOptions_title();

	String roundOptionPage_roundOptions_title();

	String roundOptionPage_title();

	String roundPlayerPage_availableCardsSection_title();

	String roundPlayerPage_buttonPostGame_title();

	String roundPlayerPage_chat_backup_announcement();

	String roundPlayerPage_chat_backup_fail();

	String roundPlayerPage_chat_backup_success();

	String roundPlayerPage_chat_checkpoint_reached_part1();

	String roundPlayerPage_chat_checkpoint_reached_part2();

	String roundPlayerPage_chat_darksideaward();

	String roundPlayerPage_chat_dead();

	String roundPlayerPage_chat_exitRound();

	String roundPlayerPage_chat_hamsteraward();

	String roundPlayerPage_chat_powerdown_abort();

	String roundPlayerPage_chat_powerdown_end();

	String roundPlayerPage_chat_powerdown_is();

	String roundPlayerPage_chat_powerdown_start();

	String roundPlayerPage_chat_presseraward();

	String roundPlayerPage_chat_samaritanaward();

	String roundPlayerPage_chatSection_title();

	String roundPlayerPage_exit_text();

	String roundPlayerPage_imageSize();

	String roundPlayerPage_menueSection_music_title();

	String roundPlayerPage_menueSection_musicOn_title();

	String roundPlayerPage_menueSection_sound_title();

	String roundPlayerPage_menueSection_soundOn_title();

	String roundPlayerPage_menueSection_title();

	String roundPlayerPage_otherPlayersSection_title();

	String roundPlayerPage_ready_title();

	String roundPlayerPage_switchMenueButton_title();

	String roundPlayerPage_title();

	String roundPlayerPage_winner_nobody();

	String roundPlayerPage_winner_you();

	String roundWatcherPage_title();

	String statistic_abortedgames_title();

	String statistic_highscorerank_title();

	String statistic_lostgames_title();

	String statistic_playedgames_title();

	String statistic_playingboard_title();

	String statistic_points_title();

	String statistic_rank_title();

	String statistic_wins_title();

	String tutorialPage_title();

	String winnerwindow_information_other();

	String winnerwindow_information_self();

	String winnerwindow_label();

	String winnerwindow_winner();

	String passwordWindow_title();

	String preGameGameInitiatorPage_start_notAllReady();

	String chatPresenterEnterPostGame();

	String chatPresenterEnterRound();

}
