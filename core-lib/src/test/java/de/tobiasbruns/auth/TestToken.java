/**
 * Copyright (c) 2016, Tobias Bruns
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.tobiasbruns.auth;

/**
 * Created by U109359 on 19.09.2016.
 */
public class TestToken {

	public static final TbAuthToken VALID_TOKEN = new TbAuthToken();
	static {
		VALID_TOKEN.setToken(
				"eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0aGVvIiwiZXhwIjoyMzM4MjcwOTEyLCJ1c2VyIjp7InBhc3N3b3JkIjoiZ2VoZWltIiwidXNlcm5hbWUiOiJ0aGVvIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfVEVTVF9VU0VSIiwiZGVzY3JpcHRpb24iOm51bGx9XSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiZW5hYmxlZCI6dHJ1ZX0sImlhdCI6MTQ3NDM1NzMxMn0.ZrDiEJQunEh0HuVUr6fSv-FjPiWYUItdvq-pXVkRjDsKV6Jxmjctv3fyERY5n9okFnXzfqZojAb73dJkHY-FPVUAeg2HUi4mhKkP_f3SQSCzmwTOrP_4KQzWokjR-zdlFvchQxvhFywS0pvCAPDNh3Qj4VU9pi4ld4aNIt7YlDp2uy-YuE2F-0q5Y2mni4pF-TjvvHf8EUr0ndGkMhT1xhj6PK2si1d61w6wmHCPyHp_N591K-ZbZOXW_Kk3BZoyZPYdWUCp4po0TXasL9qF5PZVhx8ngGhPzxsT2wRkTcifao7iphvr8bsZx-qnh6OnefjUN8KlZpgH53Ux8tM-Dw");
	}

	public static final String EXPIRED_TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJleHAiOjE0NzQyODU3NjUsInVzZXIiOnsidXNlcklkIjoiMmFiYjJmODgtYzhjOC00ZTZlLTg4YzQtNGFlYjRkMWNmMTI0IiwiYXV0aG9yaXRpZXMiOm51bGwsInVzZXJuYW1lIjoidGVzdHVzZXIiLCJhY2NvdW50Tm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwiY3JlZGVudGlhbHNOb25FeHBpcmVkIjp0cnVlLCJlbmFibGVkIjp0cnVlfX0.mBwnbTMkLRO3ZORYCQw1SsbLgFNoj44JCQnco0u8MhADgcQlTyNdJJuqx57hS2A5WKmoxZ9iiXbq09dqmEtNcdRdzyV2i4BhM2eN65TCiLsVzKhwY5PXetAdAkYSKfZBOj28rBAgB36kqHUR2BrVesnmDljGbYwqtyboSAD3MJWCbEncpg7XAqAxmb9tG5WAM5OPxPujcmNoBARJZPhRfCCtsErg93_RN9ACXfLdkHOoWt7rxy-KwpItVTmA7X11_kvp8f9g846g1Q2B80A3bvMA4Wim5Z-TfC5hqxY-HoFPCPQazTHN7QSMlCuF7TBbchD0D3QIhISLQtyUYXF9TA";
	public static final String MANIPULATED_TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJleHAiOjE0NzQyODU3NjUsInVzZXIiOnsidXNlcklkIjoiMmFiYjJmODgtYzhjOC00ZTZlLTg4YzQtNGFlYjRkMWNmMTI0IiwiYXV0aG9yaXRpZXMiOm51bGwsInVzZXJuYW1lIjoidGVzdHVzZXIiLCJhY2NvdW50Tm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwiY3JlZGVudGlhbHNOb25FeHBpcmVkIjp0cnVlLCJlbmFibGVkIjp0cnVlfX0.mBwnbTMkLRO3ZORYCQw1SsbLgFNoj44JCQnco0u8MhADgcQlTyNdJJuqx57hS2A5WKmoxZ9iiXbq09dqmEtNcdRdzyV2i4BhM2eN65TCiLsVzKhwY5PXetAdAkYSKfZBOj28rBAgB36kqHUR2BrVesnmDljGbYwqtyboSAD3MJWCbEncpg7XAqAxmb9tG5WAM5OPxPujcmNoBARJZPhRfCCtsErg93_RN9ACXfLdkHOoWt7rxy-KwpItVTmA7X11_kvp8f9g846g1Q2B80A3bvMA4Wim5Z-TfC5hqxY-HoFPDPQazTHN7QSMlCuF7TBbchD0D3QIhISLQtyUYXF9TA";
}
