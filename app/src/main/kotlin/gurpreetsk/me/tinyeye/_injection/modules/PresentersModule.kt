package gurpreetsk.me.tinyeye._injection.modules

import dagger.Module
import dagger.Provides
import gurpreetsk.me.tinyeye._storage.contracts.RemoteRepository
import gurpreetsk.me.tinyeye.feedback.FeedbackPresenter
import gurpreetsk.me.tinyeye.feedback.FeedbackPresenterImpl
import gurpreetsk.me.tinyeye.feedback.FeedbackView

@Module class PresentersModule {
/*
  @Provides fun provideFeedbackPresenter(
      feedbackView: FeedbackView,
      remoteRepository: RemoteRepository
  ): FeedbackPresenter =
      FeedbackPresenterImpl(feedbackView, remoteRepository)
*/
}
