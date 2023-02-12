package leetcode;

import java.util.*;

/**
 * @Author Hanyu.Wang
 * @Date 2023/2/9 13:54
 * @Description
 * @Version 1.0
 **/
public class P1797 {
    public static void main(String[] args) {
        // 构造 AuthenticationManager ，设置 timeToLive = 5 秒。
        AuthenticationManager authenticationManager = new AuthenticationManager(5);

        // 时刻 1 时，没有验证码的 tokenId 为 "aaa" ，没有验证码被更新。
        authenticationManager.renew("aaa", 1);

        // 时刻 2 时，生成一个 tokenId 为 "aaa" 的新验证码。
        authenticationManager.generate("aaa", 2);

        // 时刻 6 时，只有 tokenId 为 "aaa" 的验证码未过期，所以返回 1 。
        System.out.println(authenticationManager.countUnexpiredTokens(6));

        // 时刻 7 时，生成一个 tokenId 为 "bbb" 的新验证码。
        authenticationManager.generate("bbb", 7);

        // tokenId 为 "aaa" 的验证码在时刻 7 过期，且 8 >= 7 ，所以时刻 8 的renew 操作被忽略，没有验证码被更新。
        authenticationManager.renew("aaa", 8);

        // tokenId 为 "bbb" 的验证码在时刻 10 没有过期，所以 renew 操作会执行，该 token 将在时刻 15 过期。
        authenticationManager.renew("bbb", 10);

        // tokenId 为 "bbb" 的验证码在时刻 15 过期，tokenId 为 "aaa" 的验证码在时刻 7 过期，所有验证码均已过期，所以返回 0 。
        System.out.println(authenticationManager.countUnexpiredTokens(15));
    }

    static class AuthenticationManager {

        private TreeMap<String, Integer> tokenMap = new TreeMap<>();
        private int timeToLive;

        public AuthenticationManager(int timeToLive) {
            this.timeToLive = timeToLive;
        }

        public void generate(String tokenId, int currentTime) {
            tokenMap.put(tokenId, currentTime + this.timeToLive);
        }

        public void renew(String tokenId, int currentTime) {
            Integer tokenExpireAt = tokenMap.getOrDefault(tokenId, null);
            if (tokenExpireAt == null) {
                return;
            }
            if (tokenExpireAt <= currentTime) {
                this.tokenMap.remove(tokenId);
                return;
            }
            tokenMap.put(tokenId, currentTime + this.timeToLive);
        }

        public int countUnexpiredTokens(int currentTime) {
            this.tokenMap.entrySet().removeIf(p -> p.getValue() <= currentTime);
            return this.tokenMap.size();
        }
    }

//    class Token {
//        public String token;
//        public int expireAt;
//    }

/**
 * Your AuthenticationManager object will be instantiated and called as such:
 * AuthenticationManager obj = new AuthenticationManager(timeToLive);
 * obj.generate(tokenId,currentTime);
 * obj.renew(tokenId,currentTime);
 * int param_3 = obj.countUnexpiredTokens(currentTime);
 */
}
