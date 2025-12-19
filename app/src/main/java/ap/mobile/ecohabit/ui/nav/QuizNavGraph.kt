package ap.mobile.ecohabit.ui.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import ap.mobile.ecohabit.ui.QuizFeedbackCorrectScreen
import ap.mobile.ecohabit.ui.QuizFeedbackWrongScreen
import ap.mobile.ecohabit.ui.QuizHistoryScreen
import ap.mobile.ecohabit.ui.QuizHomeScreen
import ap.mobile.ecohabit.ui.QuizQuestionScreen
import ap.mobile.ecohabit.ui.QuizResultScreen
import ap.mobile.ecohabit.viewmodel.QuizViewModel


@Composable
fun QuizNavGraph() {

    val vm: QuizViewModel = viewModel()
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = "home"
    ) {
        // HOME =======================================================
        composable("home") {
            QuizHomeScreen(
                bestScore = vm.bestScore.value,
                streak = vm.maxStreak.value,
                onStartQuiz = {
                    vm.startQuiz()
                    nav.navigate("question")
                }
            )
        }

        // QUESTION ====================================================
        composable("question") {
            val q = vm.questions[vm.currentIndex.value]

            QuizQuestionScreen(
                question = q.text,
                options = q.options,
                currentIndex = vm.currentIndex.value,
                timer = vm.timer.value,
                selectedOption = vm.selectedAnswer.value,
                onSelect = { vm.selectedAnswer.value = it },
                onSubmit = {
                    val correct = vm.submitAnswer()
                    if (correct) {
                        nav.navigate("feedback_correct")
                    } else {
                        nav.navigate("feedback_wrong")
                    }
                }
            )
        }

        // FEEDBACK – SALAH ==========================================
        composable("feedback_wrong") {
            val q = vm.questions[vm.currentIndex.value]

            QuizFeedbackWrongScreen(
                correctAnswer = q.options[q.correctIndex],
                explanation = q.explanation,
                onNext = {
                    val hasNext = vm.nextQuestion()
                    if (hasNext) nav.navigate("question")
                    else nav.navigate("result")
                }
            )
        }

        // FEEDBACK – BENAR ==========================================
        composable("feedback_correct") {
            val q = vm.questions[vm.currentIndex.value]

            QuizFeedbackCorrectScreen(
                explanation = q.explanation,
                onSeeResult = {
                    val hasNext = vm.nextQuestion()
                    if (hasNext) nav.navigate("question")
                    else nav.navigate("result")
                }
            )
        }

        // RESULT =====================================================
        composable("result") {
            QuizResultScreen(
                score = vm.score.value,
                maxStreak = vm.maxStreak.value,
                category = vm.getCategory(),
                weeklyProgress = vm.weeklyProgress.value,
                dailyTip = vm.getTodayTip(),
                onHistoryClick = {
                    nav.navigate("history")
                },
                onNext = {
                    nav.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("history") {
            vm.loadQuizHistory()
            QuizHistoryScreen(
                history = vm.quizHistory.value,
                onBack = { nav.popBackStack() }
            )
        }


    }
    }


