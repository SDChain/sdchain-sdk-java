/*
 * Copyright SDChain Alliance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sdchain.model;

import io.sdchain.exception.APIConnectionException;
import io.sdchain.exception.APIException;
import io.sdchain.exception.AuthenticationException;
import io.sdchain.exception.ChannelException;
import io.sdchain.exception.FailedException;
import io.sdchain.exception.InvalidParameterException;
import io.sdchain.exception.InvalidRequestException;
import io.sdchain.util.Utility;

/**
 * the operation of address
 */
public abstract class OperationClass extends SDChainObject {

    private String src_address;
    private String src_secret;

    public Boolean validate = true;

    // String used to identify the asy mode or sy mode
    protected static final String VALIDATED = "?validated=";

    /**
     * Get source address in the Operation
     * @return source account address
     */
    public String getSrcAddress() {
        return src_address;
    }
    /**
     * Get source secret in the Operation
     * @return src_secret
     */
    public String getSrcSecret() {
        return src_secret;
    }

    /**
     * Waiting for validated result or not
     * @return validate
     */
    public Boolean getValidate() {
        return validate;
    }

    public void setSrcAddress(String in_address) {
        src_address = in_address;
    }

    public void setSrcSecret(String in_secret) {
        src_secret = in_secret;
    }

    public void setValidate(Boolean in_bool) {
        validate = in_bool;
    }

    public abstract RequestResult submit()
        throws AuthenticationException, InvalidRequestException,
        APIConnectionException, APIException, ChannelException, InvalidParameterException, FailedException;

    public void submit(OperationListener listener) throws AuthenticationException, InvalidRequestException,
        APIConnectionException, APIException, ChannelException, InvalidParameterException, FailedException {
        Utility.callback(new OperationRunnable(this, listener));
    }

    private class OperationRunnable implements Runnable {
        private OperationClass operator;
        private OperationListener listener;

        private OperationRunnable(OperationClass operator, OperationListener listener) {
            this.operator = operator;
            this.listener = listener;
        }
        public void run() {
            try {
                RequestResult result = this.operator.submit();
                this.listener.onComplete(result);
            } catch (AuthenticationException e) {
                e.printStackTrace();
            } catch (InvalidRequestException e) {
                e.printStackTrace();
            } catch (APIConnectionException e) {
                e.printStackTrace();
            } catch (APIException e) {
                e.printStackTrace();
            } catch (ChannelException e) {
                e.printStackTrace();
            } catch (InvalidParameterException e) {
                e.printStackTrace();
            } catch (FailedException e) {
                e.printStackTrace();
            }
        }
    }

    public interface OperationListener {
        public void onComplete(RequestResult result);
    }
}
