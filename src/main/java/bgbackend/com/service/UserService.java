package bgbackend.com.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bgbackend.com.model.Bet;
import bgbackend.com.model.PoolBet;
import bgbackend.com.model.User;
import bgbackend.com.model.request.UserRequest;
import bgbackend.com.repository.BetRepository;
import bgbackend.com.repository.PoolBetRepository;
import bgbackend.com.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BetRepository betRepository;

    @Autowired
    private PoolBetRepository poolBetRepository;

    public void register(UserRequest userRequest) {
        userRepository.save(userRequest.getUser());
    }

    public void makeBet(User currentUser, Double value, PoolBet pool) {
        Optional<Bet> optional = pool.getBets().stream().filter(bet -> bet.getUserId().equals(currentUser.getId())).findAny();
        if(optional.isEmpty()) {
            Bet newBet = betRepository.save(new Bet(currentUser.getId(), value));
            pool.getBets().add(newBet);
        }
        else {
            Bet currentBet = optional.get();
            currentBet.setValue(currentBet.getValue() + value);
            betRepository.save(currentBet);
        }
        poolBetRepository.save(pool);
    }
}
